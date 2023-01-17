package com.ederfmatos.spring.usecase.impl

import com.ederfmatos.spring.exception.OrderNotFoundException
import com.ederfmatos.spring.gateway.OrderGateway
import com.ederfmatos.spring.usecase.GetOrderUseCase
import org.springframework.stereotype.Component

@Component
class GetOrderUseCaseImpl(
    private val orderGateway: OrderGateway
) : GetOrderUseCase {
    override fun execute(id: String): GetOrderUseCase.Output {
        val order = orderGateway.findById(id) ?: throw OrderNotFoundException()
        val items = order.items.map {
            GetOrderUseCase.Output.Item(
                id = it.id,
                name = it.name,
                quantity = it.quantity,
                amount = it.amount,
                totalAmount = it.totalAmount
            )
        }
        return GetOrderUseCase.Output(
            id = order.id,
            customer = order.customer,
            amount = order.amount,
            status = order.status,
            items = items
        )
    }
}