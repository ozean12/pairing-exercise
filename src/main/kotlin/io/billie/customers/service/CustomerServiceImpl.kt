package io.billie.customers.service

import io.billie.customers.data.CustomerRepository
import io.billie.customers.dto.CustomerRequest
import io.billie.customers.exception.CustomerNotFoundException
import io.billie.customers.model.Customer
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerServiceImpl (val customerRepository: CustomerRepository): CustomerService {
    override fun findCustomerByUid(uid: UUID): Customer {
        return customerRepository.findCustomerById(uid)
                .orElseThrow {CustomerNotFoundException("customer with id: $uid was not found!")}
    }

    override fun findCustomers(): List<Customer> {
        return customerRepository.findCustomers()
    }

    override fun createCustomer(customerRequest: CustomerRequest): UUID {
        return customerRepository.createCustomer(customerRequest)
    }
}