package io.billie.invoicing.data

import io.billie.invoicing.dto.InstallmentRequest
import io.billie.invoicing.dto.InvoiceRequest
import io.billie.invoicing.model.Installment
import io.billie.invoicing.model.Invoice
import io.billie.invoicing.model.InvoiceStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import kotlin.streams.toList

@Repository
class InvoiceRepository {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly = false)
    fun createCustomerInvoices(invoice: InvoiceRequest): UUID {
        val invoiceUid = createGrandInvoice(invoice)

        invoice.installmentRequests.stream()
                .forEach { createInstallmentInvoice(it, invoiceUid) }

        return invoiceUid
    }

    private fun createInstallmentInvoice(installment: InstallmentRequest, grandInvoiceUid: UUID):UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
                { connection ->
                    val ps = connection.prepareStatement(
                            """
                                INSERT INTO organisations_schema.installments 
                                    (grand_invoice_uid,order_uid, installment_amount, status,customer_uid,merchant_uid, date_created, due_date, last_update)
                                    VALUES (?,?,?,?,?,?,?,?,?)
                            """.trimIndent(),
                            arrayOf("id")
                    )
                    ps.setObject(1, grandInvoiceUid)
                    ps.setObject(2, installment.orderUid)
                    ps.setBigDecimal(3, installment.installmentAmount)
                    ps.setString(4, installment.status.name)
                    ps.setObject(5, installment.customerUid)
                    ps.setObject(6, installment.merchantUid)
                    ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()))
                    ps.setTimestamp(8, Timestamp.valueOf(installment.dueDate))
                    ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()))
                    ps
                }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun createGrandInvoice(invoice: InvoiceRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
                { connection ->
                    val ps = connection.prepareStatement(
                            """
                                INSERT INTO organisations_schema.invoices 
                                    (order_uid, order_amount, overall_status,customer_uid,merchant_uid, date_created, last_update)
                                    VALUES (?,?,?,?,?,?,?)
                            """.trimIndent(),
                            arrayOf("id")
                    )
                    ps.setObject(1, invoice.orderUid)
                    ps.setBigDecimal(2, invoice.orderAmount)
                    ps.setString(3, invoice.status.name)
                    ps.setObject(4, invoice.customerUid)
                    ps.setObject(5, invoice.merchantUid)
                    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()))
                    ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()))
                    ps
                }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    @Transactional
    fun findCustomerInvoiceByUid (invoiceUid: UUID): Optional<Invoice> {
        val invoiceOptional = jdbcTemplate.query(
                "SELECT * FROM organisations_schema.invoices WHERE id =?",
                invoiceDataMapper(),
                invoiceUid
        ).stream().findFirst()

        if (invoiceOptional.isEmpty) return Optional.empty()

        val invoice = invoiceOptional.get()

        val list = jdbcTemplate.query(
                "SELECT * FROM organisations_schema.installments where grand_invoice_uid =?",
                installmentDataMapper(invoice),
                invoiceUid
        )

        invoice.installments.addAll(list)

        return Optional.of(invoice)
    }

    @Transactional
    fun findInvoices(): List<Invoice> {
        return jdbcTemplate.query(
                "SELECT * FROM organisations_schema.invoices",
                invoiceDataMapper(),
        ).stream()
                .map { invoice ->
                    val list = jdbcTemplate.query(
                            "SELECT * FROM organisations_schema.installments where grand_invoice_uid =?",
                            installmentDataMapper(invoice),
                            invoice.id
                    )

                    invoice.installments.addAll(list)
                    invoice
                }.toList()
    }

    private fun invoiceDataMapper() = RowMapper<Invoice> { it: ResultSet, _: Int ->
        Invoice(
                it.getObject("id", UUID::class.java),
                it.getObject("order_uid", UUID::class.java),
                it.getBigDecimal("order_amount"),
                InvoiceStatus.valueOf(it.getString("overall_status")),
                it.getObject("customer_uid", UUID::class.java),
                it.getObject("merchant_uid", UUID::class.java),
                mutableListOf(),
                it.getTimestamp("date_created").toLocalDateTime(),
                it.getTimestamp("last_update").toLocalDateTime(),
        )
    }

    private fun installmentDataMapper(invoice:Invoice) = RowMapper<Installment> { it: ResultSet, _: Int ->
        Installment(
                it.getObject("id", UUID::class.java),
                invoice,
                it.getObject("order_uid", UUID::class.java),
                it.getBigDecimal("installment_amount"),
                InvoiceStatus.valueOf(it.getString("status")),
                it.getObject("customer_uid", UUID::class.java),
                it.getObject("merchant_uid", UUID::class.java),
                it.getTimestamp("date_created").toLocalDateTime(),
                it.getTimestamp("last_update").toLocalDateTime(),
                it.getTimestamp("due_date").toLocalDateTime()
        )
    }
}