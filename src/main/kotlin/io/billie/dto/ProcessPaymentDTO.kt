package io.billie.dto

import java.util.UUID

data class ProcessPaymentDTO(
    val paymentOrderId: UUID,
    val shippedItemsCount: Int,
    val amountToPay: Double
)
