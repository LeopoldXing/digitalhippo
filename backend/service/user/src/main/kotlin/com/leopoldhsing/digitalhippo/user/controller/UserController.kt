package com.leopoldhsing.digitalhippo.user.controller

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.user.service.UserService
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "REST APIs for User Information", description = "REST APIs for retrieving user information")
@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(private val userService: UserService) {

    @Operation(
        summary = "Get Current User",
        description = "Retrieve the information of the currently authenticated user."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User information retrieved successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = User::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "User not authenticated",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @GetMapping
    fun getUser(): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser())
    }
}
