package com.leopoldhsing.digitalhippo.stripe;

import com.leopoldhsing.digitalhippo.stripe.config.StripeProperties;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(basePackages = "com.leopoldhsing.digitalhippo")
@EnableFeignClients(basePackages = "com.leopoldhsing.digitalhippo.feign")
@EnableConfigurationProperties({StripeProperties.class})
@SpringBootApplication
public class StripeApplication {
    public static void main(String[] args) {
        SpringApplication.run(StripeApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("Stripe Microservice REST API Documentation")
                        .description("DigitalHippo Backend Stripe Microservice REST API Documentation")
                        .version("1.0.0")
                        .contact(
                                new Contact()
                                        .name("Leopold Hsing")
                                        .email("leopoldhsing@gmail.com")
                                        .url("https://www.linkedin.com/in/leopoldhsing/")
                        )
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
        ).externalDocs(
                new ExternalDocumentation()
                        .description("DigitalHippo project documentation")
                        .url("https://blogs.leopoldhsing.com")
        );
    }
}
