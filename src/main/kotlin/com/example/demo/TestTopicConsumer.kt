package com.example.demo

import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.event.ListenerContainerIdleEvent
import org.springframework.stereotype.Component

@Component
class TestTopicConsumer(
    private val listenerHandler: ListenerHandler,
) {

    private val logger = KotlinLogging.logger {}

    @KafkaListener(
        id = "listener_1",
        topics = ["test-topic"],
        groupId = "demo",
        autoStartup = "false",
        containerFactory = "shortIdleEventIntervalContainerFactory"
    )
    fun listener1Handler(
        message: String,
    ) {
        logger.info { "Listener 1 consuming message: $message" }
    }

    @KafkaListener(
        id = "listener_2",
        topics = ["test-topic"],
        groupId = "demo",
        autoStartup = "false",
        containerFactory = "shortIdleEventIntervalContainerFactory"
    )
    fun listener2Handler(
        message: String,
    ) {
        logger.info { "Listener 2 consuming message: $message" }
    }

    @EventListener
    fun onAppStarted(event: ApplicationStartedEvent?) {
        listenerHandler.start("listener_1")
        listenerHandler.start("listener_2")
    }

    @EventListener(condition = """event.listenerId.startsWith("listener_1")""")
    fun idleEventRequeueHandler(event: ListenerContainerIdleEvent) {
        listenerHandler.pause("listener_1")
    }

    @EventListener(condition = """event.listenerId.startsWith("listener_2")""")
    fun idleEventClearHandler(event: ListenerContainerIdleEvent) {
        listenerHandler.pause("listener_2")
    }
}
