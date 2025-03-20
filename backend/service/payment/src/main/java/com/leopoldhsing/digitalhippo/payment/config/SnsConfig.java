package com.leopoldhsing.digitalhippo.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

import java.net.URI;

@Configuration
public class SnsConfig {

    private final String accessKey;
    private final String secretKey;
    private final String region;
    private final String snsEndpoint;

    public SnsConfig(Environment env) {
        this.accessKey = env.getProperty("spring.cloud.aws.credentials.access-key");
        this.secretKey = env.getProperty("spring.cloud.aws.credentials.secret-key");
        this.region = env.getProperty("spring.cloud.aws.region.static");
        this.snsEndpoint = env.getProperty("spring.cloud.aws.sns.endpoint");
    }

    @Bean
    public software.amazon.awssdk.services.sns.SnsClient getSnsClient() {
        return software.amazon.awssdk.services.sns.SnsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .endpointOverride(URI.create(snsEndpoint == null ? "http://localstack:4566" : snsEndpoint))
                .region(Region.of(region))
                .build();
    }
}
