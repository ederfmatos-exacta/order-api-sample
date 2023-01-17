package com.ederfmatos.spring.usecase.impl

import com.ederfmatos.spring.exception.OrderNotFoundException
import com.ederfmatos.spring.gateway.OrderGateway
import com.ederfmatos.spring.messaging.EventEmitter
import com.ederfmatos.spring.usecase.FinishOrderUseCase
import org.springframework.stereotype.Component

@Component
class FinishOrderUseCaseImpl(
    private val orderGateway: OrderGateway,
    private val eventEmitter: EventEmitter
) : FinishOrderUseCase {
    override fun execute(id: String) {
        val order = orderGateway.findById(id) ?: throw OrderNotFoundException()
        order.finish()
        orderGateway.finish(order)
        eventEmitter.emit("ORDER_FINISHED", order)
    }
}