package com.leopoldhsing.digitalhippo.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {
    private String secretKey;
    private String publishableKey;
    private String frontendEndpoint;
    private String webhookSecret;

    public StripeProperties() {
    }

    public StripeProperties(String secretKey, String publishableKey, String frontendEndpoint, String webhookSecret) {
        this.secretKey = secretKey;
        this.publishableKey = publishableKey;
        this.frontendEndpoint = frontendEndpoint;
        this.webhookSecret = webhookSecret;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public String getFrontendEndpoint() {
        return frontendEndpoint;
    }

    public void setFrontendEndpoint(String frontendEndpoint) {
        this.frontendEndpoint = frontendEndpoint;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }

    public void setWebhookSecret(String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        StripeProperties that = (StripeProperties) o;
        return Objects.equals(secretKey, that.secretKey) && Objects.equals(publishableKey, that.publishableKey) && Objects.equals(frontendEndpoint, that.frontendEndpoint) && Objects.equals(webhookSecret, that.webhookSecret);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(secretKey);
        result = 31 * result + Objects.hashCode(publishableKey);
        result = 31 * result + Objects.hashCode(frontendEndpoint);
        result = 31 * result + Objects.hashCode(webhookSecret);
        return result;
    }
}
