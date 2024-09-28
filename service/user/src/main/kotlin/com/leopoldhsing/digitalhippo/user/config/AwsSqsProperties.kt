package com.leopoldhsing.digitalhippo.user.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "aws.sqs")
open class AwsSqsProperties(
    var name: String = ""
)
