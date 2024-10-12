package com.leopoldhsing.digitalhippo.cart.repository

import com.leopoldhsing.digitalhippo.model.entity.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository : JpaRepository<Cart, Long> {
    fun findCartsByUserId(userId: Long): Collection<Cart>
    fun findCartsByUserIdAndProductIdIn(userId: Long, productId: Collection<Long>): List<Cart>
    fun deleteCartByProductIdAndUserId(userId: Long, productId: Long)
}