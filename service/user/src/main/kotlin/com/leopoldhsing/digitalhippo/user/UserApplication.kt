package com.leopoldhsing.digitalhippo.user

import com.leopoldhsing.digitalhippo.model.audit.AuditAwareImpl
import com.leopoldhsing.digitalhippo.user.config.AwsSnsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = ["com.leopoldhsing.digitalhippo"])
@EnableFeignClients(basePackages = ["com.leopoldhsing.digitalhippo.feign"])
@EnableConfigurationProperties(AwsSnsProperties::class)
open class UserApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<UserApplication>(*args)
        }
    }

    @Bean
    open fun userAuditorAware(): AuditorAware<String> {
        return AuditAwareImpl()
    }
}