package com.leopoldhsing.digitalhippo.user.controller

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.model.vo.UserCreationVo
import com.leopoldhsing.digitalhippo.model.vo.UserLoginResponseVo
import com.leopoldhsing.digitalhippo.model.vo.UserLoginVo
import com.leopoldhsing.digitalhippo.user.service.UserService
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.CollectionUtils
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*

@Tag(name = "Authentication APIs for Users", description = "REST APIs to sign in, sign up, sign out, and verify email.")
@RestController
@RequestMapping("/api/user")
class AuthController @Autowired constructor(private val userService: UserService) {

    @Operation(
        summary = "User Sign In",
        description = "Authenticate user credentials and sign in."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Sign in successful",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserLoginResponseVo::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Authentication failed",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PostMapping("/sign-in")
    fun signIn(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User login credentials",
            required = true,
            content = [Content(schema = Schema(implementation = UserLoginVo::class))]
        )
        @RequestBody userLoginVo: UserLoginVo
    ): ResponseEntity<UserLoginResponseVo> {
        val email = userLoginVo.email
        val password = userLoginVo.password
        var productIdList = userLoginVo.productIdList
        if (CollectionUtils.isEmpty(productIdList)) {
            productIdList = ArrayList()
        }

        return ResponseEntity.status(HttpStatus.OK).body(userService.signIn(email, password, productIdList))
    }

    @Operation(
        summary = "User Sign Out",
        description = "Sign out the current user."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Sign out successful"
            ),
            ApiResponse(
                responseCode = "401",
                description = "User not authenticated",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PostMapping("/sign-out")
    fun signOut(): ResponseEntity<Void> {
        userService.signOut()
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "User Sign Up",
        description = "Register a new user."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "User created successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = User::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "User already exists or invalid input",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PostMapping("/sign-up")
    fun createUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User registration details",
            required = true,
            content = [Content(schema = Schema(implementation = UserCreationVo::class))]
        )
        @RequestBody userCreationVo: UserCreationVo
    ): ResponseEntity<User> {
        val email = userCreationVo.email
        val password = userCreationVo.password
        val payloadId = userCreationVo.payloadId
        var productIdList = userCreationVo.productIdList
        if (CollectionUtils.isEmpty(productIdList)) {
            productIdList = ArrayList()
        }
        var role = UserRole.USER
        if (StringUtils.hasLength(userCreationVo.role)) {
            if ("admin".equals(userCreationVo.role, ignoreCase = true)) {
                role = UserRole.ADMIN
            }
        }

        val user = userService.createUser(payloadId, email, password, role, productIdList)

        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }
}
