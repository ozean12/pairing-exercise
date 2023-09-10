package io.billie.invoices.service

import io.billie.invoices.data.InvoiceRepository
import io.billie.invoices.dto.response.FullInvoiceResponse
import io.billie.invoices.dto.request.InvoiceRequest
import io.billie.invoices.mapper.toResponse
import io.billie.invoices.model.Invoice
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Service for managing invoices.
 * Provides number of functions for storing and getting invoices from repo.
 */
@Service
class InvoiceService(val db: InvoiceRepository) {

    fun findInvoices() = db.findInvoices().map { it.toResponse() }

    fun createInvoice(invoiceReq: InvoiceRequest): FullInvoiceResponse {
        val invoiceUUID = db.createInvoiceIfNeeded(invoiceReq.orderId, LocalDateTime.now())
        return db.getFullInvoice(invoiceUUID).toResponse()
    }

}