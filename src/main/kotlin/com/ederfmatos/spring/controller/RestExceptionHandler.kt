package com.ederfmatos.spring.controller

import com.ederfmatos.spring.exception.OrderAlreadyCancelledException
import com.ederfmatos.spring.exception.OrderAlreadyFinishedException
import com.ederfmatos.spring.exception.OrderNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    data class ErrorResponse(val message: String)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = [OrderNotFoundException::class])
    fun handleNotFound(exception: RuntimeException): ErrorResponse {
        return ErrorResponse(exception.message!!)
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = [OrderAlreadyFinishedException::class, OrderAlreadyCancelledException::class])
    fun handleConflict(exception: RuntimeException): ErrorResponse {
        return ErrorResponse(exception.message!!)
    }

}