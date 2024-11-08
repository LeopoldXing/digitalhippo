package com.leopoldhsing.digitalhippo.user.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient

@Configuration
open class SnsConfig(private val env: Environment) {

    private val accessKey: String? = env.getProperty("spring.cloud.aws.credentials.access-key")
    private val secretKey: String? = env.getProperty("spring.cloud.aws.credentials.secret-key")
    private val region: String? = env.getProperty("spring.cloud.aws.region.static")

    @Bean
    open fun getSnaClient(): SnsClient {
        return SnsClient.builder()
            .credentialsProvider(AwsCredentialsProvider { AwsBasicCredentials.create(accessKey, secretKey) })
            .region(Region.of(region))
            .build()
    }
}