package com.leopoldhsing.digitalhippo.notification

import com.leopoldhsing.digitalhippo.notification.config.EmailProperties
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@EntityScan(basePackages = ["com.leopoldhsing.digitalhippo"])
@EnableConfigurationProperties(EmailProperties::class)
open class NotificationApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<NotificationApplication>(*args)
        }
    }

    @Bean
    open fun customOpenAPI(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("Notification Microservice REST API Documentation")
                .description("DigitalHippo Backend Email Microservice REST API Documentation")
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
