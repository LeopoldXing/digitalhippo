package com.leopoldhsing.digitalhippo.product.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.leopoldhsing.digitalhippo.common.constants.RedisConstants
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.product.service.CacheService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.concurrent.TimeUnit

@Service
class CacheServiceImpl constructor(
    private val stringRedisTemplate: StringRedisTemplate
) : CacheService {

    override fun getDataFromCache(cacheKey: String): Any? {
        // 1. get product JSON string
        val cachedProductString: String? = stringRedisTemplate.opsForValue().get(cacheKey)

        // 2. get product object
        var data: Any? = null
        if (StringUtils.hasLength(cachedProductString)) {
            // cache hit
            val mapper = ObjectMapper()
            mapper.registerModule(JavaTimeModule())
            data = mapper.readValue(cachedProductString, Product::class.java)
        }

        return data
    }

    override fun saveDataToCache(cacheKey: String, data: Any?, ttl: Long, timeUnit: TimeUnit) {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        stringRedisTemplate.opsForValue().set(cacheKey, mapper.writeValueAsString(data), ttl, timeUnit)
    }

    override fun hasData(bitmapKey: String, identifier: Long): Boolean {
        return stringRedisTemplate.opsForValue().getBit(bitmapKey, identifier)
    }

    override fun addDataToBitmap(identifier: Long) {
        stringRedisTemplate.opsForValue().setBit(RedisConstants.PRODUCT_BITMAP_KEY + identifier, identifier, true)
    }

    override fun removeDataFromCache(cacheKey: String) {
        stringRedisTemplate.opsForValue().getAndDelete(cacheKey)
    }

    override fun removeDataFromBitmap(identifier: Long) {
        stringRedisTemplate.opsForValue().setBit(RedisConstants.PRODUCT_BITMAP_KEY + identifier, identifier, false)
        stringRedisTemplate.opsForValue().getAndDelete(RedisConstants.PRODUCT_PREFIX + RedisConstants.CACHE_SUFFIX + identifier)
    }

}