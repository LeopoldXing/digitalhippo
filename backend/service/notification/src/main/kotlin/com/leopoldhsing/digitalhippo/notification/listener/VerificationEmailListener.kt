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
class VerificationEmailListener(
    private val notificationService: NotificationService
) {

    companion object {
        private val log = getLogger(VerificationEmailListener::class.java)
    }

    @KafkaListener(topics = ["user"], groupId = "verification-group")
    fun listen(message: String) {
        // 1. Create an ObjectMapper instance and register the JavaTimeModule for proper date/time handling
        val mapper = ObjectMapper().apply { registerModule(JavaTimeModule()) }

        // 2. Deserialize the received JSON message into a SnsMessageDto object directly
        val kafkaMessageDto: KafkaMessageDto = mapper.readValue(message, KafkaMessageDto::class.java)

        // 3. Check if the message type is VERIFICATION
        if (kafkaMessageDto.type == NotificationType.VERIFICATION) {
            log.info("Received verification email message from Kafka topic 'user': {}", message)
            // 4. Send the verification email using the NotificationService
            notificationService.sendVerificationEmail(kafkaMessageDto.email, kafkaMessageDto.verificationToken)
        }
    }
}
