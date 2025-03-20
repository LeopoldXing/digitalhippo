package com.leopoldhsing.digitalhippo.cart.controller

import com.leopoldhsing.digitalhippo.cart.service.CartService
import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.model.entity.Cart
import com.leopoldhsing.digitalhippo.model.entity.Product
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "REST APIs for Cart Management", description = "REST APIs for managing shopping cart")
@RestController
@RequestMapping("/api/cart")
class CartController @Autowired constructor(
    private val cartService: CartService
) {

    @Operation(
        summary = "Add a product to cart",
        description = "Add a single product to the current user's cart."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product added to cart successfully"),
            ApiResponse(
                responseCode = "404",
                description = "User or product not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PostMapping("/{productId}")
    fun addToCart(
        @Parameter(description = "Product ID to add", example = "1", required = true)
        @PathVariable productId: Long
    ) {
        cartService.addToCart(productId)
    }

    @Operation(
        summary = "Add multiple products to cart",
        description = "Add a list of products to the current user's cart."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Products added to cart successfully"),
            ApiResponse(
                responseCode = "404",
                description = "User or products not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PostMapping
    fun addToCart(
        @Parameter(description = "List of product IDs to add", required = true)
        @RequestBody productIdList: List<Long>
    ) {
        cartService.addToCart(productIdList)
    }

    @Operation(
        summary = "Get all cart items for current user",
        description = "Retrieve all products in the current user's cart."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cart items retrieved successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = Product::class, type = "array"))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @GetMapping
    fun getAllCartItemByUser(): ResponseEntity<Collection<Product>> {
        val cartItemList: Collection<Cart> = cartService.getAllCartItemByUser()
        return ResponseEntity.ok().body(cartItemList.map { it.product })
    }

    @Operation(
        summary = "Delete a product from cart",
        description = "Remove a product from the current user's cart."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product removed from cart successfully"),
            ApiResponse(
                responseCode = "404",
                description = "User or product not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @DeleteMapping("/{productId}")
    fun deleteFromCart(
        @Parameter(description = "Product ID to remove", example = "1", required = true)
        @PathVariable productId: Long
    ) {
        cartService.deleteFromCart(productId)
    }
}
