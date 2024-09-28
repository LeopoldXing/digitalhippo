package com.leopoldhsing.digitalhippo.user.controller

import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.model.vo.UserCreationVo
import com.leopoldhsing.digitalhippo.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {
    @PostMapping
    fun createUser(@RequestBody userCreationVo: UserCreationVo): ResponseEntity<User> {
        val email = userCreationVo.email
        val password = userCreationVo.password
        var role = UserRole.USER
        if (StringUtils.hasLength(userCreationVo.role)) {
            if ("admin".equals(userCreationVo.role, ignoreCase = true)) {
                role = UserRole.ADMIN
            }
        }

        val user = userService.createUser(email, password, role);

        return ResponseEntity.status(HttpStatus.OK).body<User>(user);
    }
}
