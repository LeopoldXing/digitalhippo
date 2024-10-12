package com.leopoldhsing.digitalhippo.user.service.impl

import com.leopoldhsing.digitalhippo.common.constants.RedisConstants
import com.leopoldhsing.digitalhippo.common.exception.AuthenticationFailedException
import com.leopoldhsing.digitalhippo.common.exception.ResourceNotFoundException
import com.leopoldhsing.digitalhippo.common.exception.UserAlreadyExistsException
import com.leopoldhsing.digitalhippo.common.exception.VerificationTokenExpiredException
import com.leopoldhsing.digitalhippo.common.utils.InputSanitizeString
import com.leopoldhsing.digitalhippo.common.utils.PasswordUtil
import com.leopoldhsing.digitalhippo.common.utils.RequestUtil
import com.leopoldhsing.digitalhippo.common.utils.SignInTokenUtil
import com.leopoldhsing.digitalhippo.common.utils.VerificationTokenUtil
import com.leopoldhsing.digitalhippo.feign.cart.CartFeignClient
import com.leopoldhsing.digitalhippo.model.dto.AddToCartDto
import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.model.vo.ProductImageVo
import com.leopoldhsing.digitalhippo.model.vo.ProductVo
import com.leopoldhsing.digitalhippo.model.vo.UserLoginResponseVo
import com.leopoldhsing.digitalhippo.user.config.AwsSnsProperties
import com.leopoldhsing.digitalhippo.user.repository.UserRepository
import com.leopoldhsing.digitalhippo.user.service.UserService
import io.awspring.cloud.sns.core.SnsTemplate
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import java.util.concurrent.TimeUnit

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val snsTemplate: SnsTemplate,
    private val redisTemplate: StringRedisTemplate,
    private val awsSnsProperties: AwsSnsProperties,
    private val cartFeignClient: CartFeignClient
) : UserService {
    override fun getUser(): User {
        val userId = RequestUtil.getUid()

        val user = userRepository.findById(userId).orElseThrow { ResourceNotFoundException("User", "id", userId.toString()) }
        return user
    }

    /**
     * user sign in
     */
    override fun signIn(email: String, password: String, cartIdList: List<Long>): UserLoginResponseVo {
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
        val cartItems: List<ProductVo> = cartFeignClient.getItems(user.id)?.map { cartItem ->
            val product = cartItem.product
            val imageVoList: List<ProductImageVo> = product.productImages?.map { image ->
                val imageVo = ProductImageVo()
                BeanUtils.copyProperties(image, imageVo)
                imageVo.fileType = image.fileType.value
                imageVo
            } ?: emptyList()
            val productVo = ProductVo()
            BeanUtils.copyProperties(product, productVo)
            productVo.productImages = imageVoList
            productVo
        } ?: emptyList()

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
    override fun createUser(originalEmail: String, password: String, role: UserRole, cartItemIdList: List<Long>): User {
        // 1. Determine if the email already exists
        val email = InputSanitizeString.sanitizeString(originalEmail)
        val userOptional = userRepository.findUserByEmail(email)
        if (userOptional?.isPresent!!) {
            throw UserAlreadyExistsException(email)
        }

        // 2. Calculate hashed password
        val salt = PasswordUtil.generateSalt()
        val hashedPassword = PasswordUtil.hashPassword(InputSanitizeString.sanitizeString(password), salt)

        // 3. Construct user object
        val newUser = User(email, email, hashedPassword, salt, role, false, false)

        // 4. Save to database
        val savedUser = userRepository.save(newUser)

        // 5. persist the items in user's cart
        if (!CollectionUtils.isEmpty(cartItemIdList)) {
            cartFeignClient.addItem(AddToCartDto(savedUser, cartItemIdList))
        }

        // 6. send verification email
        // generate verification token
        val verificationToken = VerificationTokenUtil.generateVerificationToken()
        // put verification token into AWS ElastiCache
        redisTemplate.opsForValue()
            .set(
                RedisConstants.VERIFICATION_TOKEN_PREFIX + RedisConstants.VERIFICATION_TOKEN_SUFFIX + verificationToken,
                savedUser.id.toString(),
                3,
                TimeUnit.DAYS
            )
        // construct sns notification
        val emailVerificationParams: Map<String, String> =
            mapOf("type" to "verification", "email" to savedUser.email, "verificationToken" to verificationToken)

        // send message to AWS SNS
        snsTemplate.sendNotification(awsSnsProperties.arn, emailVerificationParams, "verification email")

        // 7. Return result
        return savedUser
    }

    /**
     * verify user email
     */
    override fun verifyEmail(token: String): Boolean {
        // 1. get verification token key
        val verificationTokenKey = RedisConstants.VERIFICATION_TOKEN_PREFIX + RedisConstants.VERIFICATION_TOKEN_SUFFIX + token

        // 2. search token in ElastiCache
        val hasVerificationToken =
            redisTemplate.hasKey(verificationTokenKey)
        if (!hasVerificationToken) {
            throw VerificationTokenExpiredException(token)
        }

        // 3. check the user id
        val userId = redisTemplate.opsForValue().get(verificationTokenKey)

        // 4. change userId to verified
        if (userId != null) {
            val user = userRepository.findById(userId.toLong()).orElseThrow { ResourceNotFoundException("User", "userId", userId) }
            user.isVerified = true
            userRepository.save(user)
        } else {
            throw ResourceNotFoundException("User", "userId", userId)
        }

        // 6. return result
        return true;
    }
}
