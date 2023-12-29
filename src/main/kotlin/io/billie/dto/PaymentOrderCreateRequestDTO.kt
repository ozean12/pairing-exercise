package io.billie.dto

import java.util.UUID
import javax.validation.constraints.NotNull


data class PaymentOrderCreateRequestDTO(
    @NotNull
    val organisationId: UUID,
    val itemsCount: Int,
    val totalPrice: Double,
    val currencyCode: String,
    val cardDetails: CardDetailsDTO
)