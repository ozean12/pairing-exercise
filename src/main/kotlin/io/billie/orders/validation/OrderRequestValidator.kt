package io.billie.orders.validation

import io.billie.orders.dto.OrderRequest
import io.billie.orders.execption.InvalidOrderAmountException
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class OrderRequestValidator {

    fun validateOrderRequest(orderRequest: OrderRequest) {
        if (orderRequest.amount <= BigDecimal.valueOf(0))
            throw InvalidOrderAmountException ("order amount is not valid!")

        //TODO: further validation can be placed here
    }
}