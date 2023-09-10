package io.billie.invoices.data

import com.google.gson.Gson
import io.billie.invoices.data.exceptions.NoOrderForInvoice
import io.billie.invoices.model.Product
import io.billie.invoices.model.FullInvoice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Repository
class InvoiceRepository {

    private val gson = Gson()

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional
    fun createInvoiceIfNeeded(orderId: UUID, timestamp: LocalDateTime): UUID {
        val maybeInvoiceId = findInvoiceIfExists(orderId)
        if (maybeInvoiceId != null)
            return maybeInvoiceId
        else {
            try {
                val keyHolder: KeyHolder = GeneratedKeyHolder()
                jdbcTemplate.update(
                    { connection ->
                        val ps = connection.prepareStatement(
                            "INSERT INTO organisations_schema.invoices(order_id, invoice_timestamp) VALUES(?, ?)",
                            arrayOf("id")
                        )
                        ps.setObject(1, orderId)
                        ps.setTimestamp(2, Timestamp.valueOf(timestamp))
                        ps
                    },
                    keyHolder
                )
                return keyHolder.getKeyAs(UUID::class.java)!!
            } catch (e: DataIntegrityViolationException) {
                throw NoOrderForInvoice("No order for invoice creation is found: ${e.message}")
            }
        }
    }

    @Transactional(readOnly=true)
    fun findInvoices(): List<FullInvoice> = jdbcTemplate.query(fullInvoiceQuery(""), fullInvoiceMapper())

    @Transactional(readOnly = true)
    fun getFullInvoice(invoiceUUID: UUID): FullInvoice {
        val specificIdWhereClause = "WHERE i.id = '$invoiceUUID'"
        return jdbcTemplate.query(fullInvoiceQuery(specificIdWhereClause), fullInvoiceMapper()).first()
    }


    private fun findInvoiceIfExists(orderId: UUID): UUID? {
        val maybeInvoiceId: UUID? = jdbcTemplate.query(
            "select id from organisations_schema.invoices i WHERE i.order_id = ?",
            ResultSetExtractor {
                if (it.next()) it.getObject(1, UUID::class.java)
                else null
            },
            orderId
        )
        return maybeInvoiceId
    }

    private fun fullInvoiceQuery(optionalIdFilter: String) =
        """
            SELECT 
            i.id as id, 
            i.is_paid as is_paid,
            i.invoice_timestamp as invoice_timestamp,
            i.order_id as order_id,
            org.name as organisation_name,
            org.VAT_number as organisation_VAT_number,
            o.customer_name as customer_name,
            o.customer_VAT_number as customer_VAT_number,
            o.customer_address as customer_address,
            o.total_gross as total_gross,
            o.products as products
            FROM organisations_schema.invoices i 
            INNER JOIN organisations_schema.orders o on i.order_id::uuid = o.order_id::uuid
            INNER JOIN organisations_schema.organisations org on o.organisation_id::uuid = org.id::uuid
            $optionalIdFilter
        """.trimIndent()

    private fun fullInvoiceMapper() = RowMapper<FullInvoice> { it: ResultSet, _: Int ->
        FullInvoice(
            it.getObject("id", UUID::class.java),
            it.getBoolean("is_paid"),
            it.getTimestamp("invoice_timestamp").toLocalDateTime(),
            it.getObject("order_id", UUID::class.java),
            it.getString("organisation_name"),
            it.getString("organisation_VAT_number"),
            it.getString("customer_name"),
            it.getString("customer_VAT_number"),
            it.getString("customer_address"),
            it.getDouble("total_gross"),
            gson.fromJson(it.getString("products"), Array<Product>::class.java).toList()
        )
    }

}