package com.leopoldhsing.digitalhippo.user

import com.leopoldhsing.digitalhippo.model.audit.AuditAwareImpl
import com.leopoldhsing.digitalhippo.user.config.AwsSqsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EntityScan(basePackages = ["com.leopoldhsing.digitalhippo"])
@EnableJpaAuditing
@EnableConfigurationProperties(AwsSqsProperties::class)
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