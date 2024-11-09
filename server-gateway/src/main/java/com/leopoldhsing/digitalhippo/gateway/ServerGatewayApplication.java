package com.leopoldhsing.digitalhippo.gateway;

import com.leopoldhsing.digitalhippo.gateway.config.GatewayUrlAuthConfig;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties(GatewayUrlAuthConfig.class)
@EntityScan(basePackages = {"com.leopoldhsing.digitalhippo"})
public class ServerGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(r -> r.path("/stripe/v3/api-docs").and().method(HttpMethod.GET).uri("lb://stripe"))
                .route(r -> r.path("/user/v3/api-docs").and().method(HttpMethod.GET).uri("lb://user"))
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new io.swagger.v3.oas.models.info.Info()
                        .title("API Gateway Service REST API Documentation")
                        .description("DigitalHippo Backend API Gateway Service REST API Documentation")
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
