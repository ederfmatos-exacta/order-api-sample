package com.ederfmatos.spring.usecase.impl

import com.ederfmatos.spring.gateway.OrderGateway
import com.ederfmatos.spring.usecase.ListOrdersUseCase
import org.springframework.stereotype.Component

@Component
class ListOrdersUseCaseImpl(
    private val orderGateway: OrderGateway
) : ListOrdersUseCase {
    override fun execute(): List<ListOrdersUseCase.Output> {
        return orderGateway.list().map {
            ListOrdersUseCase.Output(
                id = it.id,
                customer = it.customer,
                status = it.status,
                amount = it.amount
            )
        }
    }
}