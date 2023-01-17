package com.ederfmatos.spring.repositories

import com.ederfmatos.spring.entities.Money
import com.ederfmatos.spring.entities.Order
import com.ederfmatos.spring.entities.OrderItem
import com.ederfmatos.spring.entities.OrderStatus
import com.ederfmatos.spring.gateway.OrderGateway
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Entity(name = "orders")
class OrderJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    lateinit var uuid: String
    lateinit var customer: String

    @Enumerated(EnumType.STRING)
    lateinit var status: OrderStatus
    lateinit var amount: BigDecimal

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    lateinit var items: List<OrderItemJpa>
}

@Entity(name = "order_items")
class OrderItemJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    lateinit var uuid: String
    lateinit var name: String
    var quantity: Int? = null
    lateinit var amount: BigDecimal

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var order: OrderJpa
}

@Repository
@ConditionalOnProperty(value = ["repository.type"], havingValue = "jpa")
internal interface OrderRepository : JpaRepository<OrderJpa, Int> {
    fun findByUuid(uuid: String): OrderJpa?
}

@Component
@ConditionalOnProperty(value = ["repository.type"], havingValue = "jpa")
internal class OrderJpaRepository(
    private val orderRepository: OrderRepository
) : OrderGateway {

    override fun list(): List<Order> {
        return orderRepository.findAll().map(::mapToOrder)
    }

    private fun mapToOrder(order: OrderJpa): Order {
        return Order(
            id = order.uuid,
            customer = order.customer,
            items = order.items.map {
                OrderItem(
                    id = it.uuid,
                    name = it.name,
                    quantity = it.quantity!!,
                    amount = Money(it.amount)
                )
            }
        ).also {
            if (order.status == OrderStatus.CANCELLED) it.cancel()
            if (order.status == OrderStatus.FINISHED) it.finish()
        }
    }

    @Transactional
    override fun create(order: Order) {
        val orderJpa = OrderJpa().also {
            it.uuid = order.id
            it.customer = order.customer
            it.status = order.status
            it.amount = order.amount.value
            it.items = order.items.map { orderItem ->
                OrderItemJpa().also { item ->
                    item.uuid = orderItem.id
                    item.name = orderItem.name
                    item.quantity = orderItem.quantity.toInt()
                    item.amount = orderItem.amount.value
                    item.order = it
                }
            }
        }
        orderRepository.save(orderJpa)
    }

    override fun findById(id: String): Order? {
        val order = orderRepository.findByUuid(id) ?: return null
        return mapToOrder(order)
    }

    override fun cancel(order: Order) {
        val orderJpa = orderRepository.findByUuid(order.id) ?: return
        orderJpa.status = order.status
        orderRepository.save(orderJpa)
    }

    override fun finish(order: Order) {
        val orderJpa = orderRepository.findByUuid(order.id) ?: return
        orderJpa.status = order.status
        orderRepository.save(orderJpa)
    }
}