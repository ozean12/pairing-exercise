package io.billie.merchants.mapper

import io.billie.merchants.dto.MerchantResponse
import io.billie.merchants.model.Merchant

fun Merchant.toMerchantResponse(): MerchantResponse =
        MerchantResponse(
                id = this.id,
                name = this.name
        )