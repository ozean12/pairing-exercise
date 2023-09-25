package io.billie.invoicing.service

import io.billie.customers.model.Customer
import org.springframework.stereotype.Service

@Service
class InstallmentService {

    // TODO: This should be aligned with the installment strategy based on customer profile
    fun getInstallmentsForCustomer (customer: Customer): Long {
        return 2L
    }
}