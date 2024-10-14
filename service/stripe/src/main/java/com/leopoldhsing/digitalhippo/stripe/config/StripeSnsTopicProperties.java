package com.leopoldhsing.digitalhippo.stripe.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "aws.sns")
public class StripeSnsTopicProperties {
    private String arn = "";
}
