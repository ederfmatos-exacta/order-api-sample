package com.ederfmatos.spring.gateway

import com.ederfmatos.spring.entities.Order

interface OrderGateway {
    fun list(): List<Order>
    fun create(order: Order)
    fun findById(id: String): Order?
    fun cancel(order: Order)
    fun finish(order: Order)
}