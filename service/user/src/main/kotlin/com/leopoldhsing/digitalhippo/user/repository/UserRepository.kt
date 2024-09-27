package com.leopoldhsing.digitalhippo.user.repository

import com.leopoldhsing.digitalhippo.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
}