package io.billie.organisations.orders.data

import io.billie.organisations.orders.domain.*
import io.billie.organisations.viewmodel.Entity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

/**
 * Offering service to query or create [Order]
 */
@Repository
class OrderRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun findById(orderId: OrderId): Order? {
        val orders = jdbcTemplate.query(orderQueryById(orderId), OrderExtractor())
        return if (orders!!.isEmpty()) null else orders[0]
    }

    fun findByOrganisation(organisationId: Entity): List<Order> {
        if(!validateOrganisation(organisationId)) {
            throw OrganisationNotFoundException(organisationId)
        }
        return jdbcTemplate.query(orderQuery(organisationId), OrderExtractor())?: listOf()
    }

    fun create(order: Order): OrderId {
        if(!validateOrganisation(order.organisationId)) {
            throw OrganisationNotFoundException(order.organisationId)
        }
        val orderId = OrderId(createOrder(order))
        order.shipments.stream().forEach {  shipment -> addShipment(orderId, shipment)}
        return orderId
    }

    fun update(order: Order): ShipmentId? {
        val shipment = order.shipments.stream()
                .filter { s -> s.shipmentId == null }
                .findFirst()
        return if (shipment.isPresent) ShipmentId(addShipment(order.orderId!!, shipment.get())) else null
    }

    private fun addShipment(orderId: OrderId, shipment: Shipment): UUID {
        return createShipment(orderId, shipment)
    }

    private fun validateOrganisation(organisationId: Entity): Boolean {
        val reply: Int? = jdbcTemplate.query(
                { connection ->
                    val ps = connection.prepareStatement(
                            "select count(1) from organisations_schema.organisations " +
                                    "WHERE id = ? "
                    )
                    ps.setObject(1, organisationId.id)
                    ps
                },
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            }
        )
        return (reply != null) && (reply > 0)
    }

    private fun createOrder(order: Order): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "INSERT INTO organisations_schema.orders (" +
                            "organisation_id, " +
                            "amount" +
                            ") VALUES (?, ?)",
                    arrayOf("id")
                )
                ps.setObject(1, order.organisationId.id)
                ps.setBigDecimal(2, order.orderAmount.amount)
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun createShipment(orderId: OrderId, shipment: Shipment): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
                { connection ->
                    val ps = connection.prepareStatement(
                            "INSERT INTO organisations_schema.shipments (" +
                                    "order_id, " +
                                    "amount" +
                                    ") VALUES (?, ?)",
                            arrayOf("id")
                    )
                    ps.setObject(1, orderId.id)
                    ps.setBigDecimal(2, shipment.shipmentAmount.amount)
                    ps
                }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun orderQuery(organisationId: Entity) = "SELECT " +
            "orders.id as order_id, " +
            "orders.organisation_id as organisation_id, " +
            "orders.amount as order_amount, " +
            "shipments.id as shipment_id, " +
            "shipments.amount as shipment_amount " +
            "FROM " +
            "organisations_schema.orders orders " +
            "LEFT JOIN organisations_schema.shipments shipments ON orders.id = shipments.order_id " +
            "WHERE orders.organisation_id = '" + organisationId.id.toString() + "'"

    private fun orderQueryById(orderId: OrderId) = "SELECT " +
            "orders.id as order_id, " +
            "orders.organisation_id as organisation_id, " +
            "orders.amount as order_amount, " +
            "shipments.id as shipment_id, " +
            "shipments.amount as shipment_amount " +
            "FROM " +
            "organisations_schema.orders orders " +
            "LEFT JOIN organisations_schema.shipments shipments ON orders.id = shipments.order_id " +
            "where orders.id = '" + orderId.id.toString() + "'"

    class OrderExtractor : ResultSetExtractor<List<Order>> {
        override fun extractData(rs: ResultSet): List<Order>? {

            val orderMap = HashMap<OrderId, Order>()

            while (rs.next()) {
                var orderId = OrderId(rs.getObject("order_id", UUID::class.java))
                var order = orderMap.get(orderId)
                if (order == null) {
                    order = Order(
                            orderId,
                            Entity(rs.getObject("organisation_id", UUID::class.java)),
                            OrderAmount(rs.getBigDecimal("order_amount")),
                            mutableSetOf()
                    )
                    orderMap.put(orderId, order)
                }
                var shipmentId = rs.getObject("shipment_id", UUID::class.java)
                if (shipmentId != null) {
                    order.addShipment(Shipment(
                            ShipmentId(shipmentId),
                            orderId,
                            ShipmentAmount(rs.getBigDecimal("shipment_amount"))
                    ))
                }
            }
            return orderMap.values.toList()
        }
    }
}
