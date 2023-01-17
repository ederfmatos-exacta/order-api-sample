package com.ederfmatos.spring.messaging

interface EventEmitter {
    fun emit(event: String, data: Any)
}