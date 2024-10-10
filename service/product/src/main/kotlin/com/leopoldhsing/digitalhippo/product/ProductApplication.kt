package com.leopoldhsing.digitalhippo.product

import com.leopoldhsing.digitalhippo.model.audit.AuditAwareImpl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = ["com.leopoldhsing.digitalhippo.feign"])
@EntityScan(basePackages = ["com.leopoldhsing.digitalhippo"])
open class ProductApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ProductApplication>(*args)
        }
    }

    @Bean
    open fun productAuditorAware(): AuditorAware<String> {
        return AuditAwareImpl()
    }
}