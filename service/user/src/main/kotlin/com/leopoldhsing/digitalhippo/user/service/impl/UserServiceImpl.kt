package com.leopoldhsing.digitalhippo.user.service.impl

import com.leopoldhsing.digitalhippo.common.utils.PasswordUtil
import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.user.repository.UserRepository
import com.leopoldhsing.digitalhippo.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
) : UserService {

    override fun createUser(email: String, password: String, role: UserRole): User {
        // 1. Determine if the email already exists
        userRepository.findUserByEmail(email)?.let {
            throw IllegalArgumentException("Email already exists")
        }

        // 2. Calculate hashed password
        val salt = PasswordUtil.generateSalt()
        val hashedPassword = PasswordUtil.hashPassword(password, salt)

        // 3. Construct user object
        val newUser = User(email, email, hashedPassword, String (salt), role, false, false)

        // 4. Save to database
        val savedUser = userRepository.save(newUser)

        // 5. send verification email


        // 6. Return result
        return savedUser
    }
}
