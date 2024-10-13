package com.leopoldhsing.digitalhippo.gateway.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "url.auth")
public class GatewayUrlAuthConfig {
    private String signUpUriPatterns;
    private String signInUriPatterns;
    private String signOutUriPatterns;
    private List<String> trustedUriPatterns;
    private List<String> innerUriPatterns;
    private List<String> publicUriPatterns;
}
