package io.billie.invoices.data

import com.google.gson.Gson
import io.billie.invoices.data.exceptions.DuplicatedRecord
import io.billie.invoices.data.exceptions.NoOrganisationForOrder
import io.billie.invoices.model.Order
import io.billie.invoices.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*

@Repository
class OrderRepository {

    private val gson = Gson()

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional
    fun create(order: Order) = createOrder(order)

    @Transactional(readOnly=true)
    fun findOrders(): List<Order> {
        return jdbcTemplate.query(
            "SELECT * FROM organisations_schema.orders",
            orderMapper()
        )
    }

    private fun createOrder(order: Order) {
        try {
            jdbcTemplate.update { connection ->
                val ps = connection.prepareStatement("""
                        INSERT INTO organisations_schema.orders (
                            order_id,
                            order_timestamp,
                            organisation_id,
                            customer_name,
                            customer_VAT_number,
                            customer_address,
                            total_gross,
                            products
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?::jsonb)
                    """.trimIndent()
                )
                ps.setObject(1, order.orderId)
                ps.setTimestamp(2, Timestamp.valueOf(order.orderTimestamp))
                ps.setObject(3, order.organisationId)
                ps.setString(4, order.customerName)
                ps.setString(5, order.customerVATNumber)
                ps.setString(6, order.customerAddress)
                ps.setDouble(7, order.totalGross)
                ps.setString(8, gson.toJson(order.products))
                ps
            }
        } catch (e: DuplicateKeyException) {
            throw DuplicatedRecord(order.orderId)
        } catch (e: DataIntegrityViolationException) {
            throw NoOrganisationForOrder("""
                |Looks like there is no organisation for which order is trying to be created.
                |Check if ${order.organisationId} exists on the platform and add it if needed.""".trimMargin())
        }
    }

    private fun orderMapper() = RowMapper<Order> { it: ResultSet, _: Int ->
        Order(
            it.getObject("order_id", UUID::class.java),
            it.getTimestamp("order_timestamp").toLocalDateTime(),
            it.getObject("organisation_id", UUID::class.java),
            it.getString("customer_name"),
            it.getString("customer_VAT_number"),
            it.getString("customer_address"),
            it.getDouble("total_gross"),
            gson.fromJson(it.getString("products"), Array<Product>::class.java).toList()
        )
    }

}