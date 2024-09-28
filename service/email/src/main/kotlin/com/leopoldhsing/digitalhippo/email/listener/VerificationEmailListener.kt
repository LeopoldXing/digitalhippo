package com.leopoldhsing.digitalhippo.email.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.leopoldhsing.digitalhippo.email.config.AwsSqsProperties
import com.leopoldhsing.digitalhippo.email.service.EmailService
import com.leopoldhsing.digitalhippo.model.dto.SnsMessageDto
import com.leopoldhsing.digitalhippo.model.dto.SnsNotificationDto
import com.leopoldhsing.digitalhippo.model.enumeration.NotificationType
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("emailListener")
class VerificationEmailListener @Autowired constructor(
    private val awsSqsProperties: AwsSqsProperties,
    private val emailService: EmailService
) {

    @SqsListener("#{awsSqsProperties.verificationQueueUrl}")
    fun listenToQueueOne(message: String) {
        println("Received message from queue-one: $message")
        // 1. construct notification object
        val mapper = ObjectMapper()
        val snsNotificationDto: SnsNotificationDto = mapper.readValue(message, SnsNotificationDto::class.java)
        val snsMessageString: String = snsNotificationDto.message
        val snsMessageDto: SnsMessageDto = mapper.readValue(snsMessageString, SnsMessageDto::class.java)

        // 2. determine if this message is for verification email
        if (snsMessageDto.type === NotificationType.VERIFICATION) {
            // 3. send email
            emailService.sendVerificationEmail(snsMessageDto.email, snsMessageDto.verificationToken)
        }
    }
}