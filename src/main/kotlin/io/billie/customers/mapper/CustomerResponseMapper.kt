package io.billie.customers.mapper

import io.billie.customers.dto.CustomerResponse
import io.billie.customers.model.Customer


fun Customer.toCustomerResponse(): CustomerResponse =
    CustomerResponse(
            id =  this.id,
            name = this.name,
            address = this.address
    )
