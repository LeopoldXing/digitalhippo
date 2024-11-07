package com.leopoldhsing.digitalhippo.email.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.leopoldhsing.digitalhippo.email.config.AwsSqsProperties
import com.leopoldhsing.digitalhippo.email.service.EmailService
import com.leopoldhsing.digitalhippo.model.dto.SnsMessageDto
import com.leopoldhsing.digitalhippo.model.dto.SnsNotificationDto
import com.leopoldhsing.digitalhippo.model.enumeration.NotificationType
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("receiptEmailListener")
class ReceiptEmailListener @Autowired constructor(
    private val awsSqsProperties: AwsSqsProperties,
    private val emailService: EmailService
) {

    companion object {
        private val log = getLogger(ReceiptEmailListener::class.java)
    }

    @SqsListener("#{awsSqsProperties.receiptQueueUrl}")
    fun listenToQueueOne(message: String) {
        log.info("Received message from receipt email queue: {}", message)
        // 1. construct notification object
        val mapper = ObjectMapper()
        val snsNotificationDto: SnsNotificationDto = mapper.readValue(message, SnsNotificationDto::class.java)
        val snsMessageString: String = snsNotificationDto.message
        val snsMessageDto: SnsMessageDto = mapper.readValue(snsMessageString, SnsMessageDto::class.java)

        // 2. determine if this message is for verification email
        if (snsMessageDto.type === NotificationType.RECEIPT) {
            // 3. send email
            emailService.sendReceiptEmail(snsMessageDto.email, snsMessageDto.orderPayloadId, snsMessageDto.products)
            log.info("prepare to send receipt email: {}", snsMessageDto)
        }
    }
}