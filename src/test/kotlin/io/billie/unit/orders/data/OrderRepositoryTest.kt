package io.billie.unit.orders.data

import io.billie.orders.data.OrderRepository
import io.billie.orders.model.Order
import io.billie.orders.model.OrderStatus
import io.billie.shared.PostgresqlSharedContainer
import io.billie.util.insertOrder
import io.billie.util.orderFromDatabase
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class OrderRepositoryTest : PostgresqlSharedContainer() {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Test
    fun findById_success() {

        val organizationId = UUID.randomUUID()
        val order = Order(
            id = UUID.randomUUID(),
            organizationId = organizationId,
            totalAmount = BigDecimal("1000.00"),
            status = OrderStatus.PENDING,
            noOfTotalItems = 10,
            createdAt = LocalDateTime.now()
        )

        jdbcTemplate.insertOrder(order)

        val foundOrder = orderRepository.findById(order.id)

        assertNotNull(foundOrder)
        assertEquals(order.id, foundOrder?.id)
        assertEquals(order.organizationId, foundOrder?.organizationId)
        assertEquals(order.totalAmount, foundOrder?.totalAmount)
        assertEquals(order.status, foundOrder?.status)
        assertEquals(order.noOfTotalItems, foundOrder?.noOfTotalItems)
    }

    @Test
    fun findById_notFound() {

        val orderId = UUID.randomUUID()

        val foundOrder = orderRepository.findById(orderId)

        assertNull(foundOrder)
    }

    @Test
    fun updateStatus_success() {

        val organizationId = UUID.randomUUID()
        val order = Order(
            id = UUID.randomUUID(),
            organizationId = organizationId,
            totalAmount = BigDecimal("1000.00"),
            status = OrderStatus.PENDING,
            noOfTotalItems = 10,
            createdAt = LocalDateTime.now()
        )

        jdbcTemplate.insertOrder(order)

        orderRepository.updateStatus(OrderStatus.SHIPPED, order.id)

        val updatedOrder = jdbcTemplate.orderFromDatabase(order.id)
        assertEquals(OrderStatus.SHIPPED.name, updatedOrder["status"])
    }

}