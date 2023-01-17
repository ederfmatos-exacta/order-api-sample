package com.ederfmatos.spring.entities

import com.ederfmatos.spring.exception.OrderAlreadyCancelledException
import com.ederfmatos.spring.exception.OrderAlreadyFinishedException

data class Order(
    val id: String,
    val customer: String,
    val items: List<OrderItem>
) {
    val amount: Money
        get() = items.map { it.totalAmount }.let(::Money)

    var status: OrderStatus = OrderStatus.PENDING
        private set

    private fun isCancelled(): Boolean = this.status == OrderStatus.CANCELLED
    private fun isFinished(): Boolean = this.status == OrderStatus.FINISHED

    fun cancel() {
        if (this.isCancelled()) throw OrderAlreadyCancelledException()
        if (this.isFinished()) throw OrderAlreadyFinishedException()
        this.status = OrderStatus.CANCELLED
    }

    fun finish() {
        if (this.isFinished()) throw OrderAlreadyFinishedException()
        if (this.isCancelled()) throw OrderAlreadyCancelledException()
        this.status = OrderStatus.FINISHED
    }
}
