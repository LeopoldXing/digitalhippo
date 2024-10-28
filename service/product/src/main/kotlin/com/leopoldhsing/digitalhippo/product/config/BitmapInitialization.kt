package com.leopoldhsing.digitalhippo.product.config

import com.leopoldhsing.digitalhippo.common.constants.RedisConstants
import com.leopoldhsing.digitalhippo.product.repository.ProductRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.util.CollectionUtils

@Configuration
open class BitmapInitialization constructor(
    private val redisTemplate: StringRedisTemplate,
    private val productRepository: ProductRepository
) : ApplicationListener<ApplicationReadyEvent> {

    /**
     * making sure the springboot instance fully started
     */
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        bitmapInitializer()
    }

    /**
     * initialize product id bitmap
     */
    open fun bitmapInitializer() {
        try {
            // 1. get all product ids
            val productList = productRepository.findAll()
            if (CollectionUtils.isEmpty(productList)) {
                return
            }
            val productIds = productList.map { it.id }

            // 2. set bitmap
            productIds.forEach { id ->
                redisTemplate.opsForValue().setBit(RedisConstants.PRODUCT_BITMAP_KEY + id, id, true)
            }
            println("bitmap initialization successful")
        } catch (ex: Exception) {
            println("Error during bitmap initialization: ${ex.message}")
            ex.printStackTrace()
        }
    }
}