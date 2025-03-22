package com.leopoldhsing.digitalhippo.user.api

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.model.entity.User
import com.leopoldhsing.digitalhippo.user.service.UserService
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Internal User APIs",
    description = "Private user APIs for internal microservice communication. External requests are blocked."
)
@RestController
@RequestMapping("/api/inner/user")
class UserApi @Autowired constructor(private val userService: UserService) {

    @Operation(
        summary = "Get current user",
        description = "Retrieve the current user based on uid in the request header."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "User retrieved successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = User::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "User ID not present in the request header",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @GetMapping
    fun getCurrentUser(): User {
        val user = userService.getUser()
        return user
    }
}
