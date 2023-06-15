package com.example.demo

import mu.KotlinLogging
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.stereotype.Service

@Service
class ListenerHandler(
    private val registry: KafkaListenerEndpointRegistry,
) {

    private val logger = KotlinLogging.logger {}

    fun start(listenerId: String) {
        logger.info("Starting the listener container with ID $listenerId and automatically pause it until someone call it on demand")
        val listenerContainer = registry.getListenerContainer(listenerId)
        if (listenerContainer != null) {
            listenerContainer.start()
            listenerContainer.pause()
            logger.info {
                "ListenerContainer with ID $listenerId successfully started and paused"
            }
        } else {
            logger.error {
                "Cannot start listenerContainer with ID $listenerId. It is null"
            }
        }
    }

    fun resume(listenerId: String) {
        logger.info("Resuming the listener container $listenerId")
        val listenerContainer = registry.getListenerContainer(listenerId)
        if (listenerContainer != null) {
            if (!listenerContainer.isRunning) {
                logger.error {
                    "Cannot resume listenerContainer with ID $listenerId. It is not running"
                }
            } else {
                listenerContainer.resume()
            }
        } else {
            logger.error {
                "Cannot resume listenerContainer with ID $listenerId. It is null"
            }
        }
    }

    fun pause(listenerId: String) {
        val listenerContainer = registry.getListenerContainer(listenerId)
        if (listenerContainer != null) {
            if (listenerContainer.isRunning) {
                if (!listenerContainer.isContainerPaused) {
                    if (!listenerContainer.isPauseRequested) {
                        listenerContainer.pause()
                        logger.info {
                            "ListenerContainer with ID $listenerId is not pause requested-> pause is now requested"
                        }
                    } else {
                        logger.info {
                            "ListenerContainer with ID $listenerId is pause requested-> waiting to be paused"
                        }
                    }
                } else {
                    logger.info {
                        "ListenerContainer with ID $listenerId paused successfully"
                    }
                }
            } else {
                logger.info {
                    "ListenerContainer with ID $listenerId is not running"
                }
            }
        } else {
            logger.error {
                "Trying to pause the listenerContainer with ID $listenerId but it is null"
            }
        }
    }
}
