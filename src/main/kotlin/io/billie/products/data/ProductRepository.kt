package io.billie.products.data

import io.billie.products.dto.ProductRequest
import io.billie.products.model.Product
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
class ProductRepository {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly=true)
    fun findProductById(id: UUID): Optional<Product> {
        return jdbcTemplate.query(
                "select id, name, price from organisations_schema.products where id= ?",
                productResponseMapper(),
                id
        ).stream().findFirst();
    }

    @Transactional
    fun findProducts(): List<Product> {
        return jdbcTemplate.query(
                "select id, name, price from organisations_schema.products",
                productResponseMapper()
        )
    }

    private fun productResponseMapper() = RowMapper<Product> { it: ResultSet, _: Int ->
        Product(
                it.getObject("id", UUID::class.java),
                it.getString("name"),
                it.getDouble("price")
        )
    }

    @Transactional
    fun createProduct (productRequest: ProductRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
                { connection ->
                    val ps = connection.prepareStatement(
                            """
                                INSERT INTO organisations_schema.products 
                                    (name, price)
                                    VALUES (?, ?)
                            """.trimIndent(),
                            arrayOf("id")
                    )
                    ps.setString(1, productRequest.name)
                    ps.setBigDecimal(2, productRequest.price)
                    ps
                }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }
}