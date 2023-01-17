package com.ederfmatos.spring.repositories

import com.ederfmatos.spring.entities.Money
import com.ederfmatos.spring.entities.Order
import com.ederfmatos.spring.entities.OrderItem
import com.ederfmatos.spring.entities.OrderStatus
import com.ederfmatos.spring.gateway.OrderGateway
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@ConditionalOnProperty(value = ["repository.type"], havingValue = "jdbc")
class OrderJdbcRepository(
    private val jdbcTemplate: JdbcTemplate
) : OrderGateway {

    private val orderItemMapper: RowMapper<OrderItem> = RowMapper { rs, _ ->
        OrderItem(
            id = rs.getString("uuid"),
            name = rs.getString("name"),
            quantity = rs.getInt("quantity"),
            amount = Money(rs.getBigDecimal("amount"))
        )
    }

    private val orderMapper: RowMapper<Order> = RowMapper { rs, _ ->
        val id = rs.getInt("id")
        val items = jdbcTemplate.query("""
            SELECT uuid, name, quantity, amount 
            FROM order_items 
            where order_id = $id 
            order by created_at
        """.trimIndent(), orderItemMapper)

        val status = rs.getString("status")

        val order = Order(
            id = rs.getString("uuid"),
            customer = rs.getString("customer"),
            items = items
        )
        if (OrderStatus.CANCELLED.name == status) order.cancel()
        if (OrderStatus.FINISHED.name == status) order.finish()
        order
    }

    override fun list(): List<Order> {
        return jdbcTemplate.query("SELECT id, uuid, customer, amount, status FROM orders", orderMapper)
    }

    override fun findById(id: String): Order? {
        return jdbcTemplate.query("SELECT id, uuid, customer, amount, status FROM orders where uuid = '$id' limit 1", orderMapper).firstOrNull()
    }

    @Transactional
    override fun create(order: Order) {
        jdbcTemplate.update("""
            INSERT INTO orders (uuid, customer, amount, status, created_at) 
            VALUE ('${order.id}', '${order.customer}', '${order.amount.value}', '${order.status}', NOW())
        """.trimIndent())
        if (order.items.isEmpty()) return

        val id = jdbcTemplate.queryForObject("SELECT id from orders where uuid = '${order.id}'", Int::class.java)!!
        val itemsSql = """
            INSERT INTO order_items (uuid, name, quantity, amount, order_id, created_at)
            VALUES ${order.items.joinToString(separator = ",") { "('${it.id}', '${it.name}', ${it.quantity}, '${it.amount.value}', '${id}', NOW())" }}
        """.trimIndent()
        jdbcTemplate.update(itemsSql)
    }

    override fun cancel(order: Order) {
        jdbcTemplate.update("UPDATE orders set status = '${order.status}' where uuid = '${order.id}'")
    }

    override fun finish(order: Order) {
        jdbcTemplate.update("UPDATE orders set status = '${order.status}' where uuid = '${order.id}'")
    }
}