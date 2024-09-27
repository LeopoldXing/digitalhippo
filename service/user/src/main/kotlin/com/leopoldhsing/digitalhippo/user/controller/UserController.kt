package com.leopoldhsing.digitalhippo.user.controller

import com.leopoldhsing.digitalhippo.user.service.UserService
import org.springframework.stereotype.Controller

@Controller
class UserController(private val userService: UserService) {

}
