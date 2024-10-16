package com.leopoldhsing.digitalhippo.cart.service.impl

import com.leopoldhsing.digitalhippo.cart.repository.CartRepository
import com.leopoldhsing.digitalhippo.cart.service.CartService
import com.leopoldhsing.digitalhippo.common.exception.ResourceNotFoundException
import com.leopoldhsing.digitalhippo.common.utils.RequestUtil
import com.leopoldhsing.digitalhippo.feign.user.UserFeignClient
import com.leopoldhsing.digitalhippo.model.entity.Cart
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.model.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class CartServiceImpl @Autowired constructor(
    private val cartRepository: CartRepository,
    private val userFeignClient: UserFeignClient
) : CartService {
    override fun addToCart(productId: Long) {
        addToCart(listOf<Long>(productId))
    }

    override fun addToCart(productIdList: List<Long>) {
        // 1. get user
        val user = userFeignClient.currentUser
        if (user == null) {
            throw ResourceNotFoundException("user", "id", RequestUtil.getUid().toString())
        }

        // 2. add to cart
        addToCart(user, productIdList)
    }

    override fun addToCart(user: User, productIdList: List<Long>) {
        // 1. query this user's cart items
        val existingCartItemIdList: List<Long> =
            cartRepository.findCartsByUserIdAndProductIdIn(user.id, productIdList).map { cart -> cart.id }

        // 2. construct cart object list
        val cartItemList: Collection<Cart> =
            productIdList.filter { it !in existingCartItemIdList }.map { productId -> Cart(Product(productId), user) }

        // 3. save to database
        cartRepository.saveAll(cartItemList)
    }

    override fun getAllCartItemByUser(): Collection<Cart> {
        // 1. get user
        val user = userFeignClient.currentUser
        if (user == null) {
            throw ResourceNotFoundException("user", "id", RequestUtil.getUid().toString())
        }

        // 2. get cart item
        return getAllCartItemByUser(user.id)
    }

    override fun getAllCartItemByUser(userId: Long): Collection<Cart> {
        return cartRepository.findCartsByUserId(userId)
    }

    @Transactional
    override fun deleteFromCart(productId: Long) {
        // 1. get user
        val user = userFeignClient.currentUser
        if (user == null) {
            throw ResourceNotFoundException("user", "id", RequestUtil.getUid().toString())
        }

        // 2. delete item
        cartRepository.deleteCartByProductIdAndUserId(productId, user.id)
    }

    override fun clearCart(userId: Long) {
        cartRepository.deleteCartsByUserId(userId)
    }

    @Transactional
    override fun clearCart() {
        // 1. get user
        val user = userFeignClient.currentUser
        if (user == null) {
            throw ResourceNotFoundException("user", "id", RequestUtil.getUid().toString())
        }

        // 2. delete all cart items
        clearCart(user.id)
    }

}