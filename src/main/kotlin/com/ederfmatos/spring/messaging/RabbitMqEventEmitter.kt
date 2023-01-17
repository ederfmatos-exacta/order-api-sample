package com.ederfmatos.spring.messaging

import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
internal class RabbitMqEventEmitter(
    private val rabbitTemplate: RabbitTemplate
) : EventEmitter {
    override fun emit(event: String, data: Any) {
        val message = rabbitTemplate.messageConverter.toMessage(data, MessageProperties())
        rabbitTemplate.send("amq.direct", event, message)
    }
}