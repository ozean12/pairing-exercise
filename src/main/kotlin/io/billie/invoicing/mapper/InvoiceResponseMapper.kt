package io.billie.invoicing.mapper

import io.billie.invoicing.dto.InstallmentResponse
import io.billie.invoicing.dto.InvoiceResponse
import io.billie.invoicing.model.Installment
import io.billie.invoicing.model.Invoice
import kotlin.streams.toList

fun Invoice.toInvoiceResponse(): InvoiceResponse {
        val installmentResponses = this.installments.stream()
                .map { it.toInstallmentResponse() }
                .toList()

        return InvoiceResponse(
                id = this.id,
                orderUid = this.orderUid,
                orderAmount = this.orderAmount,
                status = this.status,
                customerUid = this.customerUid,
                merchantUid = this.merchantUid,
                installments = installmentResponses
        )
}

fun Installment.toInstallmentResponse(): InstallmentResponse =
        InstallmentResponse(
                installmentAmount = this.installmentAmount,
                status = this.status,
                dueDate =this.dueDate
        )