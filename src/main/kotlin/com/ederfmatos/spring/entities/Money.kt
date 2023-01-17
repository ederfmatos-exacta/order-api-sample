package com.ederfmatos.spring.entities

import java.math.BigDecimal

data class Money(val value: BigDecimal) {

    constructor(values: Iterable<Money>) : this(values.sumOf { it.value })

    operator fun times(number: Number): Money {
        return Money(value * BigDecimal.valueOf(number.toLong()))
    }
}