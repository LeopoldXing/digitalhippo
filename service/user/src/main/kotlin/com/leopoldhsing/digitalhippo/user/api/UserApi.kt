package com.leopoldhsing.digitalhippo.user.api

import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/inner/user")
class UserApi @Autowired constructor(val userService: UserService) {

    @GetMapping
    fun getCurrentUser(): User {
        val user = userService.getUser()
        return user
    }
}