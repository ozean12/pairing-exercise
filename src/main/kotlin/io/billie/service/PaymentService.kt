package io.billie.service

import io.billie.dto.CardDetailsDTO
import java.util.UUID

interface PaymentService {

    fun holdMoney(cardDetails: CardDetailsDTO)

    fun withdrawMoney(paymentOrderId: UUID, amountToPay: Double)
}
