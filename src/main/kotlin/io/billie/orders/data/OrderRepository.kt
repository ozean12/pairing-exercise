package io.billie.orders.data

import io.billie.orders.dto.OrderRequest
import io.billie.orders.model.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.*

@Repository
class OrderRepository {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly=true)
    fun findOrderByUid(uid: UUID): Optional<Order> {
        return jdbcTemplate.query(
                "SELECT id , order_amount, customer_uid FROM organisations_schema.customer_orders where id= ?",
                orderDataMapper(),
                uid
        ).stream().findFirst();
    }

    @Transactional
    fun findOrders (): List<Order> {
        return jdbcTemplate.query(
                " SELECT id , order_amount, customer_uid FROM organisations_schema.customer_orders",
                orderDataMapper()
        )
    }

    @Transactional
    fun createOrder (orderRequest: OrderRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
                { connection ->
                    val ps = connection.prepareStatement(
                            """
                                INSERT INTO organisations_schema.customer_orders 
                                    (order_amount, customer_uid)
                                    VALUES (?, ?)
                            """.trimIndent(),
                            arrayOf("id")
                    )
                    ps.setBigDecimal(1, orderRequest.amount)
                    ps.setObject(2, orderRequest.customerId)
                    ps
                }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun orderDataMapper() = RowMapper<Order> { it: ResultSet, _: Int ->
        Order(
                it.getObject("id", UUID::class.java),
                it.getBigDecimal("order_amount"),
                it.getObject("customer_uid", UUID::class.java)
        )
    }

    private fun findOrdersQuery(whereClause: String)=
        """
            SELECT 
                id , order_amount, customer_uid
            FROM 
                organisations_schema.customer_orders
            $whereClause
            
        """.trimIndent()

}