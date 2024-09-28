package com.leopoldhsing.digitalhippo.user.service

import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole

interface UserService {

    fun createUser(email: String, password: String, role: UserRole): User

    fun verifyEmail(token: String): Boolean
}