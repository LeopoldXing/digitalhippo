package com.leopoldhsing.digitalhippo.cart.api

import com.leopoldhsing.digitalhippo.cart.service.CartService
import com.leopoldhsing.digitalhippo.model.dto.AddToCartDto
import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.model.entity.Cart
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Internal Cart APIs",
    description = "REST APIs for internal microservice communication. External access is restricted."
)
@RestController
@RequestMapping("/api/cart/inner")
class CartApi @Autowired constructor(private val cartService: CartService) {

    @Operation(
        summary = "Add items to user's cart",
        description = "Add a list of product IDs to a user's cart."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Items added to cart successfully"),
            ApiResponse(
                responseCode = "404",
                description = "User or product not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PostMapping
    fun addItems(
        @Parameter(description = "Add to cart DTO containing user and product ID list", required = true)
        @RequestBody addToCartDto: AddToCartDto
    ) {
        cartService.addToCart(addToCartDto.user, addToCartDto.productIdList)
    }

    @Operation(
        summary = "Get all cart items by user ID",
        description = "Retrieve all cart items for a specific user."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cart items retrieved successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = Cart::class, type = "array"))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @GetMapping("/{userId}")
    fun getItems(
        @Parameter(description = "User ID", example = "1", required = true)
        @PathVariable userId: Long
    ): Collection<Cart> {
        return cartService.getAllCartItemByUser(userId)
    }

    @Operation(
        summary = "Clear all users' cart items",
        description = "Remove all items from all users' carts."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "All cart items cleared successfully")
        ]
    )
    @DeleteMapping
    fun clearAllCartItems() {
        cartService.clearCart()
    }

    @Operation(
        summary = "Clear a user's cart items",
        description = "Remove all items from a specific user's cart."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User's cart items cleared successfully"),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @DeleteMapping("/{userId}")
    fun clearUserCartItems(
        @Parameter(description = "User ID", example = "1", required = true)
        @PathVariable userId: Long
    ) {
        cartService.clearCart(userId)
    }
}
