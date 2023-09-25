package io.billie.merchants.validation

import io.billie.merchants.dto.MerchantRequest
import io.billie.merchants.exception.InvalidMerchantRequestException
import org.springframework.stereotype.Component

@Component
class MerchantValidator() {

    fun validateMerchantCreationRequest(merchantRequest: MerchantRequest) {
        if (merchantRequest.name.isEmpty())
            throw InvalidMerchantRequestException ("merchant name is invalid")
    }

}
