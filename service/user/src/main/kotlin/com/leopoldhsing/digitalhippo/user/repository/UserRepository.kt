package com.leopoldhsing.digitalhippo.user.repository

import com.leopoldhsing.digitalhippo.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByEmail(email: String): User?
}