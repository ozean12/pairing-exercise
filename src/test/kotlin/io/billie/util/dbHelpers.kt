package io.billie.util

import io.billie.orders.model.Order
import io.billie.shipments.model.Shipment
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.Timestamp
import java.util.*

fun JdbcTemplate.createOrderInDatabase(order: Order) {
    this.update { connection ->
        val ps = connection.prepareStatement(
            "INSERT INTO orders_schema.orders (id, organisation_id, total_amount, status, no_of_items) VALUES (?, ?, ?, ?, ?)"
        )
        ps.setObject(1, order.id)
        ps.setObject(2, order.organizationId)
        ps.setBigDecimal(3, order.totalAmount)
        ps.setString(4, order.status.name)
        ps.setInt(5, order.noOfTotalItems)
        ps
    }
}

fun JdbcTemplate.createShipmentInDatabase(shipment: Shipment) {
    this.update { connection ->
        val ps = connection.prepareStatement(
            "INSERT INTO shipments_schema.shipments (id, order_id, shipment_amount, shipped_at) VALUES (?, ?, ?, ?)"
        )
        ps.setObject(1, shipment.id!!)
        ps.setObject(2, shipment.orderId)
        ps.setBigDecimal(3, shipment.shipmentAmount)
        ps.setTimestamp(4, Timestamp.valueOf(shipment.shippedAt))
        ps
    }
}

fun JdbcTemplate.insertOrder(order: Order) {
    this.update(
        "INSERT INTO orders_schema.orders (id, organisation_id, total_amount, status, no_of_items, created_at) VALUES (?, ?, ?, ?, ?, ?)",
        order.id, order.organizationId, order.totalAmount, order.status.name, order.noOfTotalItems, order.createdAt
    )
}


fun JdbcTemplate.shipmentFromDatabase(id: UUID): MutableMap<String, Any> =
    this.queryEntityFromDatabase("select * from shipments_schema.shipments where id = ?", id)

fun JdbcTemplate.orderFromDatabase(id: UUID): MutableMap<String, Any> =
    this.queryEntityFromDatabase("select * from orders_schema.orders where id = ?", id)

fun JdbcTemplate.queryEntityFromDatabase(sql: String, id: UUID): MutableMap<String, Any> =
    this.queryForMap(sql, id)