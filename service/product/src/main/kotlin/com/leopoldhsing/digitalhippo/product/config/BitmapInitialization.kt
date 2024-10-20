package com.leopoldhsing.digitalhippo.product.config

import com.leopoldhsing.digitalhippo.common.constants.RedisConstants
import com.leopoldhsing.digitalhippo.product.repository.ProductRepository
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
open class BitmapInitialization constructor(
    private val redisTemplate: StringRedisTemplate,
    private val productRepository: ProductRepository
) {

    /**
     * initialize product id bitmap
     */
    @PostConstruct
    open fun bitmapInitializer() {
        // 1. get all product ids
        val productIds = productRepository.findAll().map { it.id }

        // 2. set bitmap
        productIds.forEach { id ->
            redisTemplate.opsForValue().setBit(RedisConstants.PRODUCT_BITMAP_KEY + id, id, true)
        }
        println("bitmap initialization successful")
    }
}