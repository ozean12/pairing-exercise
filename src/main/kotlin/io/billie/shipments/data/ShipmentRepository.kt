package io.billie.shipments.data

import io.billie.shipments.model.Shipment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*

@Repository
class ShipmentRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional
    fun createShipment(shipment: Shipment): UUID {
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(
                "INSERT INTO shipments_schema.shipments (order_id, shipment_amount, shipped_at) VALUES (?, ?, ?)",
                arrayOf("id")
            )
            ps.setObject(1, shipment.orderId)
            ps.setBigDecimal(2, shipment.shipmentAmount)
            ps.setObject(3, Timestamp.valueOf(shipment.shippedAt))
            ps
        }, keyHolder)

        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    @Transactional(readOnly = true)
    fun findTotalShippedAmountByOrder(orderId: UUID): BigDecimal {
        val total = jdbcTemplate.queryForObject(
            "SELECT COALESCE(SUM(shipment_amount), 0) FROM shipments_schema.shipments WHERE order_id = ?",
            BigDecimal::class.java,
            orderId
        )
        return total
    }

    @Transactional(readOnly = true)
    fun findByOrderId(orderId: UUID): List<Shipment> {
        return jdbcTemplate.query(
            "SELECT id, order_id, shipment_amount, shipped_at FROM shipments_schema.shipments WHERE order_id = ?",
            shipmentMapper(),
            orderId
        )
    }

    @Transactional(readOnly = true)
    fun findById(shipmentId: UUID): Shipment? {
        return jdbcTemplate.query(
            "SELECT id, order_id, shipment_amount, shipped_at FROM shipments_schema.shipments WHERE id = ?",
            shipmentMapper(),
            shipmentId
        ).firstOrNull()
    }

    private fun shipmentMapper() = RowMapper<Shipment> { rs: ResultSet, _: Int ->
        Shipment(
            id = rs.getObject("id", UUID::class.java),
            orderId = rs.getObject("order_id", UUID::class.java),
            shipmentAmount = rs.getBigDecimal("shipment_amount"),
            shippedAt = rs.getTimestamp("shipped_at").toLocalDateTime()
        )
    }
}