package io.billie.customers.service

import io.billie.customers.dto.CustomerRequest
import io.billie.customers.model.Customer
import java.util.UUID

interface CustomerService {
    fun findCustomers(): List<Customer>
    fun findCustomerByUid (uid: UUID): Customer
    fun createCustomer(customerRequest: CustomerRequest): UUID
}