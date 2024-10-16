package com.leopoldhsing.digitalhippo.cart.api

import com.leopoldhsing.digitalhippo.cart.service.CartService
import com.leopoldhsing.digitalhippo.model.dto.AddToCartDto
import com.leopoldhsing.digitalhippo.model.entity.Cart
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart/inner")
class CartApi @Autowired constructor(val cartService: CartService) {

    @PostMapping
    fun addItems(@RequestBody addToCartDto: AddToCartDto) {
        cartService.addToCart(addToCartDto.user, addToCartDto.productIdList)
    }

    @GetMapping("/{userId}")
    fun getItems(@PathVariable userId: Long): Collection<Cart> {
        return cartService.getAllCartItemByUser(userId)
    }

    @DeleteMapping
    fun clearUserCartItems() {
        cartService.clearCart()
    }

    @DeleteMapping
    fun clearUserCartItems(userId: Long) {
        cartService.clearCart(userId)
    }
}