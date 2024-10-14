package com.leopoldhsing.digitalhippo.cart.controller

import com.leopoldhsing.digitalhippo.cart.service.CartService
import com.leopoldhsing.digitalhippo.model.entity.Cart
import com.leopoldhsing.digitalhippo.model.entity.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart")
class CartController @Autowired constructor(
    private val cartService: CartService
) {

    @PostMapping("/{productId}")
    fun addToCart(@PathVariable productId: Long) {
        cartService.addToCart(productId)
    }

    @PostMapping
    fun addToCart(@RequestBody productIdList: List<Long>) {
        cartService.addToCart(productIdList)
    }

    @GetMapping
    fun getAllCartItemByUser(): ResponseEntity<Collection<Product>> {
        val cartItemList: Collection<Cart> = cartService.getAllCartItemByUser()

        return ResponseEntity.ok().body<Collection<Product>>(cartItemList.map { it.product })
    }

    @DeleteMapping("/{productId}")
    fun deleteFromCart(@PathVariable productId: Long) {
        cartService.deleteFromCart(productId)
    }

}