package com.ederfmatos.spring.usecase.impl

import com.ederfmatos.spring.exception.OrderNotFoundException
import com.ederfmatos.spring.gateway.OrderGateway
import com.ederfmatos.spring.messaging.EventEmitter
import com.ederfmatos.spring.usecase.CancelOrderUseCase
import org.springframework.stereotype.Component

@Component
class CancelOrderUseCaseImpl(
    private val orderGateway: OrderGateway,
    private val eventEmitter: EventEmitter
) : CancelOrderUseCase {
    override fun execute(id: String) {
        val order = orderGateway.findById(id) ?: throw OrderNotFoundException()
        order.cancel()
        orderGateway.cancel(order)
        eventEmitter.emit("ORDER_CANCELLED", order)
    }
}