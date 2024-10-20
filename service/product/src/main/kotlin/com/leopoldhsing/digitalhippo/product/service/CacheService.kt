package com.leopoldhsing.digitalhippo.product.service

import java.util.concurrent.TimeUnit

interface CacheService {

    fun getDataFromCache(cacheKey: String): Any?

    fun saveDataToCache(cacheKey: String, data: Any?, ttl: Long, timeUnit: TimeUnit)

    fun hasData(bitmapKey: String, identifier: Long): Boolean

    fun addDataToBitmap(identifier: Long)

    fun removeDataFromCache(cacheKey: String)

    fun removeDataFromBitmap(identifier: Long)
}