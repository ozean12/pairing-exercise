package io.billie.orders.data

import io.billie.orders.model.Order
import io.billie.orders.model.OrderStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.*

@Repository
class OrderRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly = true)
    fun findById(orderId: UUID): Order? {
        return jdbcTemplate.query(
            "SELECT id, organisation_id, total_amount, status, no_of_items, created_at, modified_at FROM orders_schema.orders WHERE id = ?",
            orderMapper(),
            orderId
        ).firstOrNull()
    }

    @Transactional
    fun updateStatus(orderStatus: OrderStatus, orderId: UUID) {
        jdbcTemplate.update(
            "UPDATE orders_schema.orders SET status = ? WHERE id = ?",
            orderStatus.name,
            orderId
        )
    }

    private fun orderMapper() = RowMapper<Order> { rs: ResultSet, _: Int ->
        Order(
            id = rs.getObject("id", UUID::class.java),
            organizationId = rs.getObject("organisation_id", UUID::class.java),
            totalAmount = rs.getBigDecimal("total_amount"),
            status = OrderStatus.valueOf(rs.getString("status")),
            noOfTotalItems = rs.getInt("no_of_items"),
            createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
            modifiedAt = rs.getTimestamp("modified_at")?.toLocalDateTime()
        )
    }
}