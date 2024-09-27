package com.leopoldhsing.digitalhippo.user.service.impl

import com.leopoldhsing.digitalhippo.user.repository.UserRepository
import com.leopoldhsing.digitalhippo.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository
) : UserService
