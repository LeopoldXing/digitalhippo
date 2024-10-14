package com.leopoldhsing.digitalhippo.user.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "aws.sns")
open class userSnsTopicProperties(
    var arn: String = ""
)
