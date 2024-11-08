package com.leopoldhsing.digitalhippo.user.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.leopoldhsing.digitalhippo.common.constants.RedisConstants
import com.leopoldhsing.digitalhippo.common.exception.AuthenticationFailedException
import com.leopoldhsing.digitalhippo.common.exception.ResourceNotFoundException
import com.leopoldhsing.digitalhippo.common.exception.UserAlreadyExistsException
import com.leopoldhsing.digitalhippo.common.exception.VerificationTokenExpiredException
import com.leopoldhsing.digitalhippo.common.mapper.product.ProductMapper
import com.leopoldhsing.digitalhippo.common.utils.InputSanitizeString
import com.leopoldhsing.digitalhippo.common.utils.PasswordUtil
import com.leopoldhsing.digitalhippo.common.utils.RequestUtil
import com.leopoldhsing.digitalhippo.common.utils.SignInTokenUtil
import com.leopoldhsing.digitalhippo.common.utils.VerificationTokenUtil
import com.leopoldhsing.digitalhippo.feign.cart.CartFeignClient
import com.leopoldhsing.digitalhippo.model.dto.AddToCartDto
import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.model.vo.ProductVo
import com.leopoldhsing.digitalhippo.model.vo.UserLoginResponseVo
import com.leopoldhsing.digitalhippo.user.config.userSnsTopicProperties
import com.leopoldhsing.digitalhippo.user.controller.AuthController
import com.leopoldhsing.digitalhippo.user.repository.UserRepository
import com.leopoldhsing.digitalhippo.user.service.UserService
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import software.amazon.awssdk.services.sns.SnsClient
import java.util.concurrent.TimeUnit

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val redisTemplate: StringRedisTemplate,
    private val userSnsTopicProperties: userSnsTopicProperties,
    private val cartFeignClient: CartFeignClient,
    private val snsClient: SnsClient
) : UserService {

    companion object {
        private val log = getLogger(AuthController::class.java)
    }

    override fun getUser(): User {
        val userId = RequestUtil.getUid()
        val user = userRepository.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId.toString()) }
        return user
    }

    /**
     * user sign in
     */
    override fun signIn(email: String, password: String, cartIdList: List<Long>): UserLoginResponseVo {
        log.info("user $email trying to sign in")

        // 1. get user
        val userOption = userRepository.findUserByEmail(email)
        if (userOption == null || !userOption.isPresent) {
            throw ResourceNotFoundException("User", "email", email)
        }
        val user = userOption.get()

        // 2. verify password
        if (!PasswordUtil.hashPassword(password, user.salt).equals(user.passwordHash)) {
            throw AuthenticationFailedException(user.id.toString(), email)
        }

        // 3. combine cart item
        cartFeignClient.addItem(AddToCartDto(user, cartIdList))
        val cartItems: List<ProductVo> =
            cartFeignClient.getItems(user.id)?.map { cartItem -> ProductMapper.mapToProductVo(cartItem.product) } ?: emptyList()

        // 4. determine if the user already signed in
        val potentialTokenKey = RedisConstants.USER_PREFIX + RedisConstants.USERID_SUFFIX + user.id
        if (redisTemplate.hasKey(potentialTokenKey)) {
            // user already signed in, extend token valid period
            redisTemplate.expire(potentialTokenKey, RedisConstants.ACCESS_TOKEN_VALID_MINUTES, TimeUnit.MINUTES)
            val accessToken = redisTemplate.opsForValue().get(potentialTokenKey)

            return UserLoginResponseVo(accessToken, cartItems)
        }

        // 5. generate token for this session
        val accessToken = SignInTokenUtil.generateAccessToken()

        // 6. put token into ElastiCache for this session
        val signInTokenKey = RedisConstants.USER_PREFIX + RedisConstants.ACCESS_TOKEN_SUFFIX + accessToken
        // set token:userid
        redisTemplate.opsForValue().set(signInTokenKey, user.id.toString(), RedisConstants.ACCESS_TOKEN_VALID_MINUTES, TimeUnit.MINUTES)
        // set userid:token
        val signInTokenReversedKey = RedisConstants.USER_PREFIX + RedisConstants.USERID_SUFFIX + user.id
        redisTemplate.opsForValue().set(signInTokenReversedKey, accessToken, RedisConstants.ACCESS_TOKEN_VALID_MINUTES, TimeUnit.MINUTES)

        log.info("user $email signed in successful")

        return UserLoginResponseVo(accessToken, cartItems)
    }

    /**
     * user sign out
     */
    override fun signOut() {
        // 1. get userId
        val uid = RequestUtil.getUid()

        // 2. verify user login status
        if (!redisTemplate.hasKey(RedisConstants.USER_PREFIX + RedisConstants.USERID_SUFFIX + uid)) {
            return
        }

        // 3. get and delete userToken
        val tokenKey = RedisConstants.USER_PREFIX + RedisConstants.USERID_SUFFIX + uid
        val token = redisTemplate.opsForValue().get(tokenKey)
        redisTemplate.delete(tokenKey)

        // 4. remove reversed token uid
        redisTemplate.delete(RedisConstants.USER_PREFIX + RedisConstants.ACCESS_TOKEN_SUFFIX + token)
    }

    /**
     * create new user
     */
    override fun createUser(payloadId: String, originalEmail: String, password: String, role: UserRole, cartItemIdList: List<Long>): User {
        // 1. Determine if the email already exists
        val email = InputSanitizeString.sanitizeString(originalEmail)
        log.info("prepare to create user {}", email)
        val userOptional = userRepository.findUserByEmail(email)
        if (userOptional?.isPresent!!) {
            log.error("user {} already exists, sign up failed", email)
            throw UserAlreadyExistsException(email)
        }

        // 2. Calculate hashed password
        val salt = PasswordUtil.generateSalt()
        val hashedPassword = PasswordUtil.hashPassword(InputSanitizeString.sanitizeString(password), salt)

        // 3. Construct user object
        val newUser = User(payloadId, email, email, hashedPassword, salt, role, false, false)

        // 4. Save to database
        val savedUser = userRepository.save(newUser)
        log.info("user {} saved to database", savedUser.email)

        // 5. persist the items in user's cart
        if (!CollectionUtils.isEmpty(cartItemIdList)) {
            cartFeignClient.addItem(AddToCartDto(savedUser, cartItemIdList))
            log.info("save cart items for new user cart items: {}, user: {}", AddToCartDto(savedUser, cartItemIdList), savedUser.email)
        }

        // 6. send verification email
        // generate verification token
        val verificationToken = VerificationTokenUtil.generateVerificationToken()
        log.info("prepare to send verification email to new user {}, verification token: {}", savedUser.email, verificationToken)
        // put verification token into Redis
        redisTemplate.opsForValue()
            .set(
                RedisConstants.VERIFICATION_TOKEN_PREFIX + RedisConstants.VERIFICATION_TOKEN_SUFFIX + verificationToken,
                savedUser.id.toString(),
                3,
                TimeUnit.DAYS
            )
        log.info("verification token {} saved to redis, user: {}", verificationToken, savedUser.email)
        // construct sns notification
        val emailVerificationParams: Map<String, String> =
            mapOf("type" to "verification", "email" to savedUser.email, "verificationToken" to verificationToken)
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val jsonMessage = objectMapper.writeValueAsString(emailVerificationParams)

        // send message to AWS SNS
        val publishRequest = software.amazon.awssdk.services.sns.model.PublishRequest.builder()
            .messageAttributes(
                mapOf(
                    "type" to software.amazon.awssdk.services.sns.model.MessageAttributeValue.builder()
                        .dataType("String")
                        .stringValue("verification")
                        .build()
                )
            )
            .message(jsonMessage)
            .topicArn(userSnsTopicProperties.arn)
            .subject("Verification email")
            .build()

        snsClient.publish(publishRequest)

        log.info("verification email for user {} sent, params: {}", savedUser.email, publishRequest)

        // 7. Return result
        return savedUser
    }

    /**
     * verify user email
     */
    override fun verifyEmail(token: String): Boolean {
        log.info("verify email, token: {}", token)
        // 1. get verification token key
        val verificationTokenKey = RedisConstants.VERIFICATION_TOKEN_PREFIX + RedisConstants.VERIFICATION_TOKEN_SUFFIX + token

        // 2. search token in ElastiCache
        val hasVerificationToken =
            redisTemplate.hasKey(verificationTokenKey)
        if (!hasVerificationToken) {
            log.error("verify email failed, token: {}", token)
            throw VerificationTokenExpiredException(token)
        }

        // 3. check the user id
        val userId = redisTemplate.opsForValue().get(verificationTokenKey)

        // 4. change userId to verified
        if (userId != null) {
            val user = userRepository.findById(userId.toLong()).orElseThrow { ResourceNotFoundException("User", "userId", userId) }
            log.info("email verified, token: {}, user: {}", token, user)
            user.isVerified = true
            userRepository.save(user)
        } else {
            log.error("token valid, but user not found, token: {}, userId: {}", token, userId.toString())
            throw ResourceNotFoundException("User", "userId", userId)
        }

        // 6. return result
        return true;
    }
}
