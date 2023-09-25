package io.billie.functional.data

import io.billie.customers.dto.CustomerRequest

fun generateCustomerRequest(): CustomerRequest =
        CustomerRequest(
                name ="demoCustomer",
                address = "demo address"
        )