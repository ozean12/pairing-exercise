package io.billie.invoicing.service

import io.billie.invoicing.events.ShipmentEvent
import io.billie.invoicing.dto.InvoiceRequest
import io.billie.invoicing.model.Invoice
import java.util.UUID

interface InvoiceService {

    fun findInvoices(): List<Invoice>

    fun findInvoiceByUid (invoiceUid: UUID): Invoice

    fun generateInvoiceForCustomer (shipmentEvent: ShipmentEvent): InvoiceRequest

    fun saveCustomerInvoiceRequest (invoice: InvoiceRequest): UUID
}