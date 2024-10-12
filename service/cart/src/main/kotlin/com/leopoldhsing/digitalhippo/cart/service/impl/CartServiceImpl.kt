package com.leopoldhsing.digitalhippo.cart.service.impl

import com.leopoldhsing.digitalhippo.cart.repository.CartRepository
import com.leopoldhsing.digitalhippo.cart.service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CartServiceImpl @Autowired constructor(
    private val cartRepository: CartRepository
) : CartService {

}