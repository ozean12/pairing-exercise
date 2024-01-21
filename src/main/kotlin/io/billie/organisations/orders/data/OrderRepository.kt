package io.billie.organisations.orders.data

import io.billie.organisations.orders.domain.Order
import io.billie.organisations.orders.domain.OrderAmount
import io.billie.organisations.orders.domain.OrderId
import io.billie.organisations.viewmodel.Entity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
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
        val orders = jdbcTemplate.query(orderQueryById(orderId), orderMapper())
        return if (orders.isEmpty()) null else orders[0]
    }

    fun findByOrganisation(organisationId: Entity): List<Order> {
        if(!validateOrganisation(organisationId)) {
            throw OrganisationNotFoundException(organisationId)
        }
        return jdbcTemplate.query(orderQuery(organisationId), orderMapper())
    }

    fun create(order: Order): UUID {
        if(!validateOrganisation(order.organisationId)) {
            throw OrganisationNotFoundException(order.organisationId)
        }
        return createOrder(order)
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

    private fun orderQuery(organisationId: Entity) = "SELECT " +
            "orders.id as id, " +
            "orders.organisation_id as organisation_id, " +
            "orders.amount as amount " +
            "FROM " +
            "organisations_schema.orders orders " +
            "LEFT JOIN organisations_schema.shipments shipments ON orders.id = shipments.order_id " +
            "WHERE orders.organisation_id = '" + organisationId.id.toString() + "'"

    private fun orderQueryById(orderId: OrderId) = "SELECT " +
            "orders.id as id, " +
            "orders.organisation_id as organisation_id, " +
            "orders.amount as amount " +
            "FROM " +
            "organisations_schema.orders orders " +
            "LEFT JOIN organisations_schema.shipments shipments ON orders.id = shipments.order_id " +
            "where orders.id = '" + orderId.id.toString() + "'"

    private fun orderMapper() = RowMapper<Order> { it: ResultSet, _: Int ->
        Order(
            OrderId(it.getObject("id", UUID::class.java)),
            Entity(it.getObject("organisation_id", UUID::class.java)),
            OrderAmount(it.getBigDecimal("amount"))
        )
    }
}
