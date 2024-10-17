package com.leopoldhsing.digitalhippo.gateway;

import com.leopoldhsing.digitalhippo.gateway.config.GatewayUrlAuthConfig;
import io.swagger.v3.oas.models.OpenAPI;
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
        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info()
                .title("API Gateway Service")
                .description("API Gateway Service")
                .version("1.0.0"));
    }
}
