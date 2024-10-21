package com.leopoldhsing.digitalhippo.user.controller

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.model.vo.UserCreationVo
import com.leopoldhsing.digitalhippo.model.vo.UserLoginResponseVo
import com.leopoldhsing.digitalhippo.model.vo.UserLoginVo
import com.leopoldhsing.digitalhippo.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.CollectionUtils
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Authentication APIs for Users", description = "Authentication APIs to Sign-in / Sign-up / Sign-out")
@RestController
@RequestMapping("/api/user")
class AuthController @Autowired constructor(private val userService: UserService) {

    @Operation(summary = "Sign In")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sign in successfully"),
            ApiResponse(
                responseCode = "500",
                description = "Failed to Sign in",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
        ]
    )
    @PostMapping("/sign-in")
    fun signIn(@RequestBody userLoginVo: UserLoginVo): ResponseEntity<UserLoginResponseVo> {
        val email = userLoginVo.email
        val password = userLoginVo.password
        var productIdList = userLoginVo.productIdList
        if (CollectionUtils.isEmpty(productIdList)) {
            productIdList = ArrayList()
        }

        return ResponseEntity.status(HttpStatus.OK).body(userService.signIn(email, password, productIdList))
    }

    @Operation(summary = "Sign Out")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sign out successfully"),
            ApiResponse(
                responseCode = "500",
                description = "Failed to Sign out",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
        ]
    )
    @PostMapping("/sign-out")
    fun signOut(): ResponseEntity<Void> {
        userService.signOut()
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Sign Up")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User created successfully"),
            ApiResponse(
                responseCode = "500",
                description = "Failed to create user",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
        ]
    )
    @PostMapping("/sign-up")
    fun createUser(@RequestBody userCreationVo: UserCreationVo): ResponseEntity<User> {
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

        val user = userService.createUser(payloadId, email, password, role, productIdList);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Email verification")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Email verified"),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
        ]
    )
    @GetMapping("/verify-email")
    fun verifyEmail(@RequestHeader("token") token: String): ResponseEntity<Boolean> {
        val res = userService.verifyEmail(token)

        return ResponseEntity.status(200).body(res)
    }
}
