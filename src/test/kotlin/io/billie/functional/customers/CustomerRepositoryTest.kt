package io.billie.functional.customers

import io.billie.customers.data.CustomerRepository
import io.billie.functional.common.BaseIntegrationTest
import io.billie.functional.data.generateCustomerRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import java.util.UUID
import java.util.stream.IntStream

class CustomerRepositoryTest: BaseIntegrationTest() {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun resetDatabase(){
        jdbcTemplate.execute("delete from organisations_schema.customers")
    }

    @Test
    fun `should be able to create a new customer`(){
        val customerId = generateCustomer()

        Assertions.assertThat(customerId).isNotNull
    }

    private fun generateCustomer(): UUID{
        val customerRequest = generateCustomerRequest()
        return customerRepository.createCustomer(customerRequest)
    }

    @Test
    fun `should be able to find a customer by uid`(){
        // generating a customer and storing in db
        val customerId = generateCustomer()

        val customerCheck = customerRepository.findCustomerById(customerId)

        Assertions.assertThat(customerCheck.isPresent).isEqualTo(true)

        val customerFound = customerCheck.get()

        Assertions.assertThat(customerFound.id).isEqualTo(customerId)
    }

    @Test
    fun `should be able to find all customers`(){

        // generating some customers and storing in db
        IntStream.rangeClosed(0, 2).forEach { generateCustomer() }

        val customers = customerRepository.findCustomers()

        Assertions.assertThat(customers.size).isEqualTo(3)
    }
}