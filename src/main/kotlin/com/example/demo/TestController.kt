package com.example.demo

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
internal class TestController(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    @GetMapping("/test")
    fun test() {
        val producerRecord: ProducerRecord<String, String> = ProducerRecord<String, String>(
            "test-topic",
            "KEY",
            "{\"value\":\"DATA\"}",
        )
        kafkaTemplate
            .send(producerRecord)
            .get(200, TimeUnit.MILLISECONDS)
    }
}