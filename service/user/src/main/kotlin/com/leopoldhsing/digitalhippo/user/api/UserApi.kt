package com.leopoldhsing.digitalhippo.user.api

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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(
    name = "User API endpoint for other microservices",
    description = "Private user APIs, external requests will be blocked directly"
)
@RestController
@RequestMapping("/api/inner/user")
class UserApi @Autowired constructor(val userService: UserService) {

    @Operation(summary = "Get the current user based on uid in the Request")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User id successfully queried"),
            ApiResponse(
                responseCode = "500", description = "User id is not presented in the Request Header",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @GetMapping
    fun getCurrentUser(): User {
        val user = userService.getUser()
        return user
    }
}