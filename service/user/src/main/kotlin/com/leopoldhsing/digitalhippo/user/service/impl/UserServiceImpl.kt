package com.leopoldhsing.digitalhippo.user.service.impl

import com.leopoldhsing.digitalhippo.common.constants.RedisConstants
import com.leopoldhsing.digitalhippo.common.exception.ResourceNotFoundException
import com.leopoldhsing.digitalhippo.common.exception.UserAlreadyExistsException
import com.leopoldhsing.digitalhippo.common.exception.VerificationTokenExpiredException
import com.leopoldhsing.digitalhippo.common.utils.PasswordUtil
import com.leopoldhsing.digitalhippo.common.utils.VerificationTokenUtil
import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.user.config.AwsSnsProperties
import com.leopoldhsing.digitalhippo.user.repository.UserRepository
import com.leopoldhsing.digitalhippo.user.service.UserService
import io.awspring.cloud.sns.core.SnsTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val snsTemplate: SnsTemplate,
    private val redisTemplate: StringRedisTemplate,
    private val awsSnsProperties: AwsSnsProperties
) : UserService {

    /**
     * create new user
     */
    override fun createUser(email: String, password: String, role: UserRole): User {
        // 1. Determine if the email already exists
        val userOptional = userRepository.findUserByEmail(email)
        if (userOptional?.isPresent!!) {
            throw UserAlreadyExistsException(email)
        }

        // 2. Calculate hashed password
        val salt = PasswordUtil.generateSalt()
        val hashedPassword = PasswordUtil.hashPassword(password, salt)

        // 3. Construct user object
        val newUser = User(email, email, hashedPassword, String(salt), role, false, false)

        // 4. Save to database
        val savedUser = userRepository.save(newUser)

        // 5. send verification email
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

        // 6. Return result
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
