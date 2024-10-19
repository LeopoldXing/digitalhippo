package com.leopoldhsing.digitalhippo.product.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.leopoldhsing.digitalhippo.common.constants.RedisConstants
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.product.service.ProductCacheService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class ProductCacheServiceImpl constructor(
    private val stringRedisTemplate: StringRedisTemplate
) : ProductCacheService {

    override fun getProductFromCache(productId: String): Product? {
        // 1. construct cache key
        val cacheKey = RedisConstants.PRODUCT_PREFIX + RedisConstants.CACHE_SUFFIX + productId

        // 2. get product JSON string
        val cachedProductString: String? = stringRedisTemplate.opsForValue().get(cacheKey)

        // 3. get product object
        var product: Product? = null
        if (StringUtils.hasLength(cachedProductString)) {
            // cache hit
            val mapper = ObjectMapper()
            product = mapper.readValue(cachedProductString, Product::class.java)
        }

        return product
    }

    override fun saveProductToCache(productId: String, product: Product?) {
        // 1. construct cache key
        val cacheKey = RedisConstants.PRODUCT_PREFIX + RedisConstants.CACHE_SUFFIX + productId

        // 2. save product into cache
        val mapper = ObjectMapper()
        stringRedisTemplate.opsForValue().set(cacheKey, mapper.writeValueAsString(product))
    }

    override fun hasProduct(productId: Long): Boolean {
        // 1. construct cache key
        val cacheKey = RedisConstants.PRODUCT_PREFIX + RedisConstants.CACHE_SUFFIX

        // 2. query bitmap
        return stringRedisTemplate.opsForValue().getBit(cacheKey, productId)
    }

    override fun addProductToBitmap(productId: Long) {
        stringRedisTemplate.opsForValue().setBit(RedisConstants.PRODUCT_PREFIX + RedisConstants.CACHE_SUFFIX, productId, true)
    }

    override fun removeProductFromBitmap(productId: Long) {
        stringRedisTemplate.opsForValue().setBit(RedisConstants.PRODUCT_PREFIX + RedisConstants.CACHE_SUFFIX, productId, false)
    }

}