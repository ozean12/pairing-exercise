package io.billie.merchants.service

import io.billie.merchants.data.MerchantRepository
import io.billie.merchants.dto.MerchantRequest
import io.billie.merchants.exception.MerchantNotFoundException
import io.billie.merchants.model.Merchant
import org.springframework.stereotype.Service
import java.util.*

@Service
class MerchantServiceImpl (private val merchantRepository: MerchantRepository): MerchantService {
    override fun findAllMerchants(): List<Merchant> {
        return merchantRepository.findMerchants()
    }

    override fun findMerchantByUid(uid: UUID): Merchant {
        return merchantRepository.findMerchantById(uid)
                .orElseThrow { MerchantNotFoundException("merchant with id:$uid was not found!") }
    }

    override fun createMerchant(merchantRequest: MerchantRequest): UUID {
        return merchantRepository.createMerchant (merchantRequest)
    }
}