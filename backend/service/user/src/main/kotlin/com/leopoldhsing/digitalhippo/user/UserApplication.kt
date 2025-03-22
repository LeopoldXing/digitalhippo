package com.leopoldhsing.digitalhippo.user

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
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = ["com.leopoldhsing.digitalhippo"])
@EnableFeignClients(basePackages = ["com.leopoldhsing.digitalhippo.feign"])
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

    @Bean
    open fun customOpenAPI(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("User Microservice REST API Documentation")
                .description("DigitalHippo Backend User Microservice REST API Documentation")
                .version("1.0.0")
                .contact(
                    Contact()
                        .name("Leopold Hsing")
                        .email("leopoldhsing@gmail.com")
                        .url("https://www.linkedin.com/in/leopoldhsing/")
                )
                .license(License().name("MIT").url("https://opensource.org/licenses/MIT"))
        ).externalDocs(
            ExternalDocumentation()
                .description("DigitalHippo project documentation")
                .url("https://blogs.leopoldhsing.com/digitalhippo/")
        )
    }
}
