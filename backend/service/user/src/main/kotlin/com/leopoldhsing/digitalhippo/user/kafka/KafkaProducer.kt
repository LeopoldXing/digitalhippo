package com.leopoldhsing.digitalhippo.user.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {

    fun sendMessage(topic: String, message: String) {
        kafkaTemplate.send(topic, message)
    }
}
