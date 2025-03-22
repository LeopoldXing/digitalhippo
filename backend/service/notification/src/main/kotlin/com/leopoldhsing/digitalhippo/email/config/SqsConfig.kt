package com.leopoldhsing.digitalhippo.email.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import java.net.URI

@Configuration
open class SqsConfig(
    private val env: Environment
) {
    private val region: String? = env.getProperty("spring.cloud.aws.region.static")
    private val sqsEndpoint: String? = env.getProperty("spring.cloud.aws.sqs.endpoint")

    fun createSqsClient(): SqsClient {
        return SqsClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
            .region(Region.of(region))
            .endpointOverride(URI.create(if (sqsEndpoint == null) "http://localstack:4566" else sqsEndpoint))
            .build()
    }
}