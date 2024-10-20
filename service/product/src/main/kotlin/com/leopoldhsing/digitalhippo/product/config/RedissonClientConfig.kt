package com.leopoldhsing.digitalhippo.product.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@AutoConfigureAfter(RedisAutoConfiguration::class)
@Configuration
open class RedissonClientConfig constructor(
    private val redisProperties: RedisProperties
) {
    @Bean
    open fun redissonClient(): RedissonClient {
        val config: Config = Config()

        config.useSingleServer().address = "redis://${redisProperties.host}:${redisProperties.port}"
        config.useSingleServer().password = redisProperties.password

        val redissonClient = Redisson.create(config)
        return redissonClient
    }
}