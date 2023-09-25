package io.billie.merchants.data

import io.billie.merchants.dto.MerchantRequest
import io.billie.merchants.model.Merchant
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
class MerchantRepository (){
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly=true)
    fun findMerchantById(id: UUID): Optional<Merchant> {
        return jdbcTemplate.query(
                "select id, name from organisations_schema.merchants where id= ?",
                merchantResponseMapper(),
                id
        ).stream().findFirst();
    }

    @Transactional
    fun findMerchants(): List<Merchant>{
        return jdbcTemplate.query(
                "select id, name from organisations_schema.merchants",
                merchantResponseMapper()
        )
    }

    fun merchantResponseMapper() = RowMapper<Merchant> { it: ResultSet, _: Int ->
        Merchant(
                it.getObject("id", UUID::class.java),
                it.getString("name")
        )
    }

    @Transactional
    fun createMerchant(merchantRequest: MerchantRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
                { connection ->
                    val ps = connection.prepareStatement(
                            """
                                INSERT INTO organisations_schema.merchants 
                                    (name)
                                    VALUES (?)
                            """.trimIndent(),
                            arrayOf("id")
                    )
                    ps.setString(1, merchantRequest.name)
                    ps
                }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }
}