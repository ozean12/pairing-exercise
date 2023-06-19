package io.billie.orders.data

import io.billie.orders.model.OrderRequest
import io.billie.orders.model.OrderState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.*


@Repository
class OrderRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional
    fun create(order: OrderRequest): UUID {
        if (orderExists(order)) {
            throw OrderAlreadyExists(order.externalId)
        }
        return createOrder(order)
    }

    private fun createOrder(order: OrderRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
                { connection ->
                    val ps = connection.prepareStatement(
                            "INSERT INTO organisations_schema.orders (" +
                                    "external_id, " +
                                    "created_time, " +
                                    "organisation_id, " +
                                    "state " +
                                    ") VALUES (?, ?, ?, ?)",
                            arrayOf("id")
                    )
                    ps.setString(1, order.externalId)
                    ps.setTimestamp(2, Timestamp.valueOf(order.createdTime))
                    ps.setObject(3, order.organisationId)
                    ps.setString(4, OrderState.NEW.name)
                    ps
                }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun orderExists(order: OrderRequest): Boolean {
        val reply: Int? = jdbcTemplate.query(
                "select count(o.id) from organisations_schema.orders o " +
                        "WHERE o.external_id = ? " +
                        "AND o.organisation_id = ? ",
                ResultSetExtractor {
                    it.next()
                    it.getInt(1)
                },
                order.externalId,
                order.organisationId
        )
        return (reply != null) && (reply > 0)
    }

}
