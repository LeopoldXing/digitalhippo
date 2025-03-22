package com.leopoldhsing.digitalhippo.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "aws.sqs")
open class AwsSqsProperties(
    var verificationQueueName: String = "",
    var verificationQueueUrl: String = "",
    var receiptQueueName: String = "",
    var receiptQueueUrl: String = "",
)
