package com.leopoldhsing.digitalhippo.user.controller

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.model.entity.User
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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "GET API for Users", description = "Get user info")
@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(private val userService: UserService) {

    @Operation(summary = "Get the current user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User info fetched"),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
        ]
    )
    @GetMapping
    fun getUser(): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser())
    }
}