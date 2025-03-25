package com.leopoldhsing.digitalhippo.product.config

import lombok.extern.slf4j.Slf4j
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Slf4j
@AutoConfigureAfter(RedisAutoConfiguration::class)
@Configuration
open class RedissonClientConfig constructor(
    private val redisProperties: RedisProperties
) {

    companion object {
        private val log = getLogger(RedissonClientConfig::class.java)
    }

    @Bean(destroyMethod = "shutdown")
    open fun redissonClient(): RedissonClient {
        val config: Config = Config()

        log.info("Prepare to connect to Redis, Port: [ ${redisProperties.port} ]; Address: [ redis://${redisProperties.host}:${redisProperties.port} ]")

        config.useSingleServer().address = "redis://${redisProperties.host}:${redisProperties.port}"
        config.useSingleServer().password = redisProperties.password

        log.info("Successfully connected to Redis server")

        val redissonClient = Redisson.create(config)

        log.info("Redisson client created")

        return redissonClient
    }
}