package com.leopoldhsing.digitalhippo.notification.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.leopoldhsing.digitalhippo.notification.service.NotificationService
import com.leopoldhsing.digitalhippo.model.dto.KafkaMessageDto
import com.leopoldhsing.digitalhippo.model.enumeration.NotificationType
import org.slf4j.LoggerFactory.getLogger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ReceiptEmailListener(
    private val notificationService: NotificationService
) {

    companion object {
        private val log = getLogger(ReceiptEmailListener::class.java)
    }

    @KafkaListener(topics = ["receipt"], groupId = "receipt-group")
    fun listen(message: String) {
        // 1. Create an ObjectMapper instance and register the JavaTimeModule for date/time handling
        val mapper = ObjectMapper().apply { registerModule(JavaTimeModule()) }

        // 2. Deserialize the received JSON message into a SnsMessageDto object directly
        val kafkaMessageDto: KafkaMessageDto = mapper.readValue(message, KafkaMessageDto::class.java)

        // 3. Check if the message type is RECEIPT, then process it
        if (kafkaMessageDto.type == NotificationType.RECEIPT) {
            log.info("Received receipt email message from Kafka topic 'receipt': {}", message)
            // 4. Send receipt email using the NotificationService
            notificationService.sendReceiptEmail(
                kafkaMessageDto.email,
                kafkaMessageDto.orderPayloadId,
                kafkaMessageDto.products
            )
        }
    }
}
