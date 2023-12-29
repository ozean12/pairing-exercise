package io.billie.service.impl

import io.billie.dto.CardDetailsDTO
import io.billie.service.PaymentService
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentServiceImpl : PaymentService {
    override fun holdMoney(cardDetails: CardDetailsDTO) {
        //@TODO retry mechanism should be applied
        TODO("Not yet implemented")
    }

    override fun withdrawMoney(paymentOrderId: UUID, amountToPay: Double) {
        //@TODO retry mechanism should be applied
        TODO("Not yet implemented")
    }

}
