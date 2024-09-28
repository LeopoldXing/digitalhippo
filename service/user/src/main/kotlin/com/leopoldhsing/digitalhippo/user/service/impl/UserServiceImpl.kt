package com.leopoldhsing.digitalhippo.user.service.impl

import com.leopoldhsing.digitalhippo.common.constants.RedisConstants
import com.leopoldhsing.digitalhippo.common.exception.UserAlreadyExistsException
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
                RedisConstants.VERIFICATION_TOKEN_PREFIX + RedisConstants.VERIFICATION_TOKEN_SUFFIX + savedUser.id,
                verificationToken,
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
}
