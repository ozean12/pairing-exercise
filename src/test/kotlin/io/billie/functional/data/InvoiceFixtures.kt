package io.billie.functional.data

import io.billie.invoicing.dto.InstallmentRequest
import io.billie.invoicing.dto.InvoiceRequest
import io.billie.invoicing.model.InvoiceStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

fun createInvoiceRequest(orderId: UUID, customerId: UUID, merchantId: UUID): InvoiceRequest =
        InvoiceRequest(
            orderUid = orderId,
                orderAmount = BigDecimal.valueOf(200.00),
                status = InvoiceStatus.CREATED,
                customerUid = customerId,
                merchantUid = merchantId,
                createInstallments(orderId, customerId,merchantId),
                LocalDateTime.now(),
                LocalDateTime.now()
        )

fun createInstallments(orderId: UUID, customerId: UUID, merchantId: UUID): MutableList<InstallmentRequest> {
    return mutableListOf(
            createInstallmentRequest(orderId, customerId,merchantId),
            createInstallmentRequest(orderId, customerId,merchantId),
    )
}

fun createInstallmentRequest(orderId: UUID, customerId: UUID, merchantId: UUID): InstallmentRequest =
        InstallmentRequest(
                orderUid = orderId,
                installmentAmount = BigDecimal.valueOf(100.00),
                status = InvoiceStatus.CREATED,
                customerUid = customerId,
                merchantUid = merchantId,
                createdDate = LocalDateTime.now(),
                lastUpdate = LocalDateTime.now(),
                dueDate = LocalDateTime.now().plusMonths(1)
        )


