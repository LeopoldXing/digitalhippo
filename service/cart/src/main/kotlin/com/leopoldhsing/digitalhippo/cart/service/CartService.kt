package com.leopoldhsing.digitalhippo.cart.service

import com.leopoldhsing.digitalhippo.model.entity.Cart
import com.leopoldhsing.digitalhippo.model.entity.User

interface CartService {
    fun addToCart(productId: Long)
    fun addToCart(productIdList: List<Long>)
    fun addToCart(user: User, productIdList: List<Long>)
    fun getAllCartItemByUser(): Collection<Cart>
    fun getAllCartItemByUser(userId: Long): Collection<Cart>
    fun deleteFromCart(productId: Long)
    fun clearCart(userId: Long)
    fun clearCart()
}