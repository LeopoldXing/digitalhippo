package com.leopoldhsing.digitalhippo.user.service

import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.model.vo.UserLoginResponseVo

interface UserService {

    fun getUser(): User

    fun signIn(email: String, password: String, cartIdList: List<Long>): UserLoginResponseVo

    fun signOut()

    fun createUser(email: String, password: String, role: UserRole, cartItemIdList: List<Long>): User

    fun verifyEmail(token: String): Boolean
}