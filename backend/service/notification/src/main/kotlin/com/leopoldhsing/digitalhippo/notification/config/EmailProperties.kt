package com.leopoldhsing.digitalhippo.notification.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "email")
open class EmailProperties {
    var frontendEndpoint: String = ""
}