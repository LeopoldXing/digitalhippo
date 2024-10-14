package com.leopoldhsing.digitalhippo.email

import com.leopoldhsing.digitalhippo.email.config.AwsSqsProperties
import com.leopoldhsing.digitalhippo.email.config.EmailProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@EntityScan(basePackages = ["com.leopoldhsing.digitalhippo"])
@EnableConfigurationProperties(AwsSqsProperties::class, EmailProperties::class)
open class EmailApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<EmailApplication>(*args)
        }
    }
}