package com.leopoldhsing.digitalhippo.email.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "digitalhippo.email")
open class EmailProperties {
    var frontendAddress: String = ""
}