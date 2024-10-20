package com.leopoldhsing.digitalhippo.product

import com.leopoldhsing.digitalhippo.model.audit.AuditAwareImpl
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableAspectJAutoProxy
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

    @Bean
    open fun customOpenAPI(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("Product Microservice REST API Documentation")
                .description("DigitalHippo Backend Product Microservice REST API Documentation")
                .version("1.0.0")
                .contact(
                    Contact()
                        .name("Leopold Hsing")
                        .email("leopoldhsing@gmail.com")
                        .url("https://www.linkedin.com/in/leopoldhsing/")
                )
                .license(License().name("MIT").url("https://opensource.org/license/mit"))
        ).externalDocs(
            ExternalDocumentation()
                .description("DigitalHippo project documentations")
                .url("https://blogs.leopoldhsing.com")
        )
    }
}