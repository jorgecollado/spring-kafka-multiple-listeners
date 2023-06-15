package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
internal class ListenerHandlerController(
    private val listenerHandler: ListenerHandler,
) {
    @GetMapping("/resume/{listener}")
    fun test(
        @PathVariable(value = "listener")
        listener: String,
    ) {
        listenerHandler.resume(listener)
    }
}