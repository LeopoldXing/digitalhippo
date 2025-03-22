package com.leopoldhsing.digitalhippo.notification.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.leopoldhsing.digitalhippo.notification.config.AwsSqsProperties
import com.leopoldhsing.digitalhippo.notification.service.NotificationService
import com.leopoldhsing.digitalhippo.model.dto.SnsMessageDto
import com.leopoldhsing.digitalhippo.model.dto.SnsNotificationDto
import com.leopoldhsing.digitalhippo.model.enumeration.NotificationType
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("verificationEmailListener")
class VerificationEmailListener @Autowired constructor(
    private val awsSqsProperties: AwsSqsProperties,
    private val notificationService: NotificationService
) {

    companion object {
        private val log = getLogger(VerificationEmailListener::class.java)
    }

    @SqsListener("#{awsSqsProperties.verificationQueueUrl}")
    fun listenToQueueOne(message: String) {
        // 1. construct notification object
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        val snsNotificationDto: SnsNotificationDto = mapper.readValue(message, SnsNotificationDto::class.java)
        val snsMessageString: String = snsNotificationDto.message
        val snsMessageDto: SnsMessageDto = mapper.readValue(snsMessageString, SnsMessageDto::class.java)

        // 2. determine if this message is for verification email
        if (snsMessageDto.type === NotificationType.VERIFICATION) {
            log.info("Received message from verification queue: {}", message)
            // 3. send email
            notificationService.sendVerificationEmail(snsMessageDto.email, snsMessageDto.verificationToken)
        }
    }
}