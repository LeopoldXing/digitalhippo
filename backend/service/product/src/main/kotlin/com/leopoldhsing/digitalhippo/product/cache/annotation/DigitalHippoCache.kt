package com.leopoldhsing.digitalhippo.product.cache.annotation

import java.lang.annotation.Inherited
import java.util.concurrent.TimeUnit
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS

@Target(ANNOTATION_CLASS, FUNCTION)
@Retention(RUNTIME)
@Inherited
@MustBeDocumented
annotation class DigitalHippoCache(
    val cacheKey: String = "",
    val bitmapIndexExpression: String = "",
    val bitmapKeyExpression: String = "",
    /**
     * ttl = -1 means this cache never expires
     */
    val ttl: Long = 0L,
    val timeUnit: TimeUnit = TimeUnit.MILLISECONDS
)
