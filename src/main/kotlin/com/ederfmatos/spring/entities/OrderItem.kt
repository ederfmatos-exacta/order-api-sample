package com.ederfmatos.spring.entities

data class OrderItem(
    val id: String,
    val name: String,
    val quantity: Number,
    val amount: Money
) {
    val totalAmount: Money
        get() = amount * quantity
}