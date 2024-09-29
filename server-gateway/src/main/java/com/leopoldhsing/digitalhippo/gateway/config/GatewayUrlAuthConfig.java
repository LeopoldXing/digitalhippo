package com.leopoldhsing.digitalhippo.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "url.auth")
public class GatewayUrlAuthConfig {
    private String signUpUriPatterns;
    private String signInUriPatterns;
    private List<String> trustedUriPatterns;
    private List<String> innerUriPatterns;

    public GatewayUrlAuthConfig() {
    }

    public GatewayUrlAuthConfig(String signUpUriPatterns, String signInUriPatterns, List<String> trustedUriPatterns, List<String> innerUriPatterns) {
        this.signUpUriPatterns = signUpUriPatterns;
        this.signInUriPatterns = signInUriPatterns;
        this.trustedUriPatterns = trustedUriPatterns;
        this.innerUriPatterns = innerUriPatterns;
    }

    public String getSignUpUriPatterns() {
        return signUpUriPatterns;
    }

    public void setSignUpUriPatterns(String signUpUriPatterns) {
        this.signUpUriPatterns = signUpUriPatterns;
    }

    public String getSignInUriPatterns() {
        return signInUriPatterns;
    }

    public void setSignInUriPatterns(String signInUriPatterns) {
        this.signInUriPatterns = signInUriPatterns;
    }

    public List<String> getTrustedUriPatterns() {
        return trustedUriPatterns;
    }

    public void setTrustedUriPatterns(List<String> trustedUriPatterns) {
        this.trustedUriPatterns = trustedUriPatterns;
    }

    public List<String> getInnerUriPatterns() {
        return innerUriPatterns;
    }

    public void setInnerUriPatterns(List<String> innerUriPatterns) {
        this.innerUriPatterns = innerUriPatterns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GatewayUrlAuthConfig that = (GatewayUrlAuthConfig) o;
        return Objects.equals(signUpUriPatterns, that.signUpUriPatterns) && Objects.equals(signInUriPatterns, that.signInUriPatterns) && Objects.equals(trustedUriPatterns, that.trustedUriPatterns) && Objects.equals(innerUriPatterns, that.innerUriPatterns);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(signUpUriPatterns);
        result = 31 * result + Objects.hashCode(signInUriPatterns);
        result = 31 * result + Objects.hashCode(trustedUriPatterns);
        result = 31 * result + Objects.hashCode(innerUriPatterns);
        return result;
    }
}
