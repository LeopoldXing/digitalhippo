package com.leopoldhsing.digitalhippo.user.controller

import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.model.vo.UserCreationVo
import com.leopoldhsing.digitalhippo.model.vo.UserLoginVo
import com.leopoldhsing.digitalhippo.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class AuthController @Autowired constructor(private val userService: UserService) {

    @PostMapping("/sign-in")
    fun signIn(@RequestBody userLoginVo: UserLoginVo): ResponseEntity<String> {
        val email = userLoginVo.email
        val password = userLoginVo.password

        return ResponseEntity.status(HttpStatus.OK).body(userService.signIn(email, password))
    }

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

    @GetMapping("/verify-email")
    fun verifyEmail(@RequestHeader("token") token: String): ResponseEntity<Boolean> {
        val res = userService.verifyEmail(token)

        return ResponseEntity.status(200).body<Boolean>(res)
    }
}
