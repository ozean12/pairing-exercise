package io.billie.customers.data

import io.billie.customers.dto.CustomerRequest
import io.billie.customers.model.Customer
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
class CustomerRepository {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly=true)
    fun findCustomerById(id: UUID): Optional<Customer> {
        return jdbcTemplate.query(
                "select id, name, address_details from organisations_schema.customers where id= ?",
                customerResponseMapper(),
                id
        ).stream().findFirst();
    }

    private fun customerResponseMapper() = RowMapper<Customer> { it: ResultSet, _: Int ->
        Customer(
                it.getObject("id", UUID::class.java),
                it.getString("name"),
                it.getString("address_details")
        )
    }

    @Transactional
    fun findCustomers(): List<Customer> {
        return jdbcTemplate.query(
                "select id, name, address_details from organisations_schema.customers",
                customerResponseMapper(),
        )
    }

    @Transactional
    fun createCustomer(customerRequest: CustomerRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
                { connection ->
                    val ps = connection.prepareStatement(
                            """
                                INSERT INTO organisations_schema.customers 
                                    (name, address_details)
                                    VALUES (?, ?)
                            """.trimIndent(),
                            arrayOf("id")
                    )
                    ps.setString(1, customerRequest.name)
                    ps.setString(2, customerRequest.address)
                    ps
                }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }
}