package com.ederfmatos.spring.controller

import com.ederfmatos.spring.usecase.CancelOrderUseCase
import com.ederfmatos.spring.usecase.CreateOrderUseCase
import com.ederfmatos.spring.usecase.FinishOrderUseCase
import com.ederfmatos.spring.usecase.GetOrderUseCase
import com.ederfmatos.spring.usecase.ListOrdersUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
    private val createOrderUseCase: CreateOrderUseCase,
    private val getOrderUseCase: GetOrderUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val finishOrderUseCase: FinishOrderUseCase,
    private val listOrdersUseCase: ListOrdersUseCase,
) {

    @GetMapping
    fun listOrders(): List<ListOrdersUseCase.Output> {
        return listOrdersUseCase.execute()
    }

    @GetMapping("/{id}")
    fun findOrderById(@PathVariable("id") id: String): GetOrderUseCase.Output {
        return getOrderUseCase.execute(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody input: CreateOrderUseCase.Input): CreateOrderUseCase.Output {
        return createOrderUseCase.execute(input)
    }

    @PatchMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelOrder(@PathVariable("id") id: String) {
        return cancelOrderUseCase.execute(id)
    }

    @PatchMapping("/{id}/finish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun finishOrder(@PathVariable("id") id: String) {
        return finishOrderUseCase.execute(id)
    }
}