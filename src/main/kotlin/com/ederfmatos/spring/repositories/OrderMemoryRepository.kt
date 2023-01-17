package com.ederfmatos.spring.repositories

import com.ederfmatos.spring.entities.Order
import com.ederfmatos.spring.gateway.OrderGateway
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["repository.type"], havingValue = "memory")
class OrderMemoryRepository : OrderGateway {
    private val orders: MutableList<Order> = mutableListOf()

    override fun list(): List<Order> {
        return orders
    }

    override fun create(order: Order) {
        orders.add(order)
    }

    override fun findById(id: String): Order? {
        return orders.find { it.id == id }
    }

    override fun cancel(order: Order) {
        val index = orders.indexOfFirst { it.id == order.id }
        orders[index] = order
    }

    override fun finish(order: Order) {
        val index = orders.indexOfFirst { it.id == order.id }
        orders[index] = order
    }
}