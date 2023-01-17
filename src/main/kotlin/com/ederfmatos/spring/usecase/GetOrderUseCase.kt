package com.ederfmatos.spring.usecase

import com.ederfmatos.spring.entities.Money
import com.ederfmatos.spring.entities.OrderStatus

interface GetOrderUseCase {
    data class Output(val id: String, val customer: String, val amount: Money, val status: OrderStatus, val items: List<Item>) {
        data class Item(val id: String, val name: String, val quantity: Number, val amount: Money, val totalAmount: Money)
    }
    fun execute(id: String): Output
}