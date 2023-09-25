package io.billie.merchants.service

import io.billie.merchants.dto.MerchantRequest
import io.billie.merchants.model.Merchant
import java.util.*

interface MerchantService {
    fun findAllMerchants(): List<Merchant>
    fun findMerchantByUid (uid: UUID): Merchant
    fun createMerchant(merchantRequest: MerchantRequest): UUID
}