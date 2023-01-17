package com.ederfmatos.spring.usecase

import com.ederfmatos.spring.entities.Money
import com.ederfmatos.spring.entities.OrderStatus

interface ListOrdersUseCase {
    data class Output(val id: String, val customer: String, val status: OrderStatus, val amount: Money)
    fun execute(): List<Output>
}