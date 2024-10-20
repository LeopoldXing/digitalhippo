package com.leopoldhsing.digitalhippo.product.cache.aspect

import com.leopoldhsing.digitalhippo.common.constants.RedisConstants
import com.leopoldhsing.digitalhippo.common.exception.ResourceNotFoundException
import com.leopoldhsing.digitalhippo.product.cache.annotation.DigitalHippoCache
import com.leopoldhsing.digitalhippo.product.service.CacheService
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.expression.common.TemplateParserContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.util.ObjectUtils
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

@Aspect
@Component
class CacheAspect constructor(
    private val cacheService: CacheService,
    private val redissonClient: RedissonClient
) {

    @Pointcut("@annotation(com.leopoldhsing.digitalhippo.product.cache.annotation.DigitalHippoCache)")
    fun cachePointcut() {
    }

    @Around("cachePointcut()")
    fun cacheAroundInterceptor(pjp: ProceedingJoinPoint): Any? {
        // 1. get invoker's params
        val args: Array<out Any?>? = pjp.args

        var lockKey = ""
        var rLock: RLock? = null
        var locked = false
        val identifier: Long = args!![0] as Long
        try {
            // 1. get data from cache
            val cacheKey = determineCacheKey(pjp)
            val cachedData = cacheService.getDataFromCache(cacheKey)
            if (!ObjectUtils.isEmpty(cachedData)) {
                // cache hit
                return cachedData
            } else {
                // 2. cache miss
                val digitalhippoCacheAnnotation = getAnnotation(pjp, DigitalHippoCache::class.java)
                // 2.1 verify this product existence in Product Bitmap
                var returnValue: Any? = null
                val bitmapKey = RedisConstants.PRODUCT_BITMAP_KEY + identifier
                if (cacheService.hasData(bitmapKey, identifier)) {
                    // Bitmap shows this data exists
                    // 2.1.1 Prepare to query data in Postgres
                    // before connect to the database, this request needs to get a Distributed Lock for this piece of data
                    lockKey = RedisConstants.LOCK_PREFIX + cacheKey
                    rLock = redissonClient.getLock(lockKey)
                    // try lock
                    locked = rLock.tryLock()
                    if (locked) {
                        // successfully get the lock for this product, this means no other requests are trying to query this data
                        returnValue = pjp.proceed()
                        // Save record into cache with expiration
                        var ttl = digitalhippoCacheAnnotation.ttl
                        var unit = digitalhippoCacheAnnotation.timeUnit
                        if (ttl < 0L) ttl = -1
                        if (unit == null) unit = RedisConstants.DEFAULT_TIME_UNIT
                        cacheService.saveDataToCache(cacheKey, returnValue, ttl, unit)
                        // Return data
                        return returnValue
                    } else {
                        // not be able to get the lock, this means another request is trying to query this product
                        Thread.sleep(300)
                        return cacheService.getDataFromCache(cacheKey)
                    }
                } else {
                    // Bitmap shows this data doesn't exist
                    // save this data into cache, even if it doesn't exist, this will prevent someone maliciously send request for this data to comprise the database
                    cacheService.saveDataToCache(cacheKey, null, -1L, TimeUnit.MILLISECONDS)
                    // someone is trying to access a data that doesn't exist
                    throw ResourceNotFoundException("product", "id", identifier.toString())
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            throw RuntimeException(e)
        } finally {
            if (locked) {
                rLock?.unlock()
            }
        }
    }

    /**
     * Determine cacheKey based on the provided pjp
     *
     * @param pjp
     * @return cacheKey
     */
    private fun determineCacheKey(pjp: ProceedingJoinPoint): String {
        val digitalhippoCacheAnnotation = getAnnotation(pjp, DigitalHippoCache::class.java)
        // Get the attribute value of the annotation
        val cacheKeyExpression = digitalhippoCacheAnnotation.cacheKey
        val cacheKey = expressionCalculator(cacheKeyExpression, pjp)
        return cacheKey
    }

    /**
     * Get the annotation from the advised method
     * @param pjp
     * @param annotationClass
     * @return
     */
    private fun <T : Annotation> getAnnotation(pjp: ProceedingJoinPoint, annotationClass: Class<T>): T {
        // Get the GmallCache annotation's parameters
        val methodSignature = pjp.signature as MethodSignature
        // Get the method
        val method = methodSignature.method
        // Get the annotation
        return method.getDeclaredAnnotation(annotationClass)
    }

    /**
     * Calculate the expression
     */
    private fun expressionCalculator(cacheKeyExpression: String, pjp: ProceedingJoinPoint): String {
        // Parse the expression (using default delimiters)
        // val expression: Expression = expressionParser.parseExpression(cacheKeyExpression, ParserContext.TEMPLATE_EXPRESSION)
        // Parse the expression (using custom delimiters)
        val expression = SpelExpressionParser().parseExpression(cacheKeyExpression, TemplateParserContext("\${", "}"))

        // Context
        val context = StandardEvaluationContext()
        val args = pjp.args
        context.setVariable("args", args)

        // Calculate value
        val value = expression.getValue(context, String::class.java)

        return value
    }

    /**
     * Get the return type of the method
     */
    private fun getMethodReturnType(pjp: ProceedingJoinPoint): Type {
        val methodSignature = pjp.signature as MethodSignature
        val genericReturnType = methodSignature.method.genericReturnType
        return genericReturnType
    }

}