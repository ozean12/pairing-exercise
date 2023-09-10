package io.billie.invoices.mapper

import io.billie.invoices.dto.CustomerOverview
import io.billie.invoices.dto.response.FullInvoiceResponse
import io.billie.invoices.dto.OrganisationOverview
import io.billie.invoices.model.FullInvoice
import java.time.format.DateTimeFormatter

fun FullInvoice.toResponse(): FullInvoiceResponse = FullInvoiceResponse(
    id = this.id,
    orderId = this.orderId,
    isPaid = this.isPaid,
    formattedTime = this.invoiceTimestamp.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
    organisation = OrganisationOverview(this.organisationName, this.organisationVATNumber),
    customer = CustomerOverview(this.customerName, this.customerVATNumber, this.customerAddress),
    totalGross = this.totalGross,
    products = this.products
)