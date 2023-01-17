package com.ederfmatos.spring.usecase.impl

import com.ederfmatos.spring.entities.Order
import com.ederfmatos.spring.entities.OrderItem
import com.ederfmatos.spring.gateway.OrderGateway
import com.ederfmatos.spring.messaging.EventEmitter
import com.ederfmatos.spring.usecase.CreateOrderUseCase
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CreateOrderUseCaseImpl(
    private val orderGateway: OrderGateway,
    private val eventEmitter: EventEmitter
) : CreateOrderUseCase {
    override fun execute(input: CreateOrderUseCase.Input): CreateOrderUseCase.Output {
        val items = input.items.map {
            OrderItem(
                id = UUID.randomUUID().toString(),
                name = it.name,
                quantity = it.quantity,
                amount = it.amount
            )
        }
        val order = Order(
            id = UUID.randomUUID().toString(),
            customer = input.customer,
            items = items
        )
        orderGateway.create(order)
        eventEmitter.emit("ORDER_CREATED", order)
        return mapToOutput(order)
    }

    private fun mapToOutput(order: Order): CreateOrderUseCase.Output {
        val items = order.items.map {
            CreateOrderUseCase.Output.Item(
                id = it.id,
                name = it.name,
                quantity = it.quantity,
                amount = it.amount,
                totalAmount = it.totalAmount
            )
        }
        return CreateOrderUseCase.Output(
            id = order.id,
            customer = order.customer,
            amount = order.amount,
            status = order.status,
            items = items
        )
    }
}