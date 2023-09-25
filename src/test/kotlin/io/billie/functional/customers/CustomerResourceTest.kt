package io.billie.functional.customers

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.customers.data.CustomerRepository
import io.billie.customers.dto.CustomerRequest
import io.billie.customers.dto.CustomerResponse
import io.billie.functional.common.BaseIntegrationTest
import io.billie.functional.data.generateCustomerRequest
import io.billie.organisations.model.Entity
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import java.util.stream.IntStream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CustomerResourceTest {

    @Autowired
    private lateinit var mocMvc: MockMvc

    @Autowired
    private lateinit var customerRepo: CustomerRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var jacksonTester: JacksonTester<CustomerRequest>

    @BeforeEach
    fun resetDatabase() {
        jdbcTemplate.execute("delete from organisations_schema.customers")
    }

    @Test
    fun `should return empty when there is no customers in database`() {
        val findCustomers = mocMvc.perform(
                get("/customers")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
                .andReturn()

        val customerResponses = objectMapper.readValue(findCustomers.response.contentAsString, Array<CustomerResponse>::class.java)

        Assertions.assertThat(customerResponses.size).isZero
    }

    @Test
    fun `should return all customers from database`() {
        IntStream.rangeClosed(0, 2)
                .forEach { generateCustomer() }

        val findAllCustomers =
                mocMvc.perform(
                        get("/customers")
                )
                        .andExpect(status().isOk)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn()

        val customerResponses = objectMapper.readValue(findAllCustomers.response.contentAsString, Array<CustomerResponse>::class.java)
        Assertions.assertThat(customerResponses.size).isEqualTo(3)
    }

    @Test
    fun `create new customer should return customer uuid`() {
        val customerRequest = generateCustomerRequest()

        val createCustomerCall = mocMvc.perform(
                post("/customers")
                        .content(jacksonTester.write(customerRequest).json)
                        .contentType(MediaType.APPLICATION_JSON)
                    )
                .andExpect(status().isCreated)
                .andReturn()

        val createResult = objectMapper.readValue(createCustomerCall.response.contentAsString, Entity::class.java)

        Assertions.assertThat(createResult.id).isNotNull
    }

    private fun generateCustomer(): UUID {
        return customerRepo.createCustomer(generateCustomerRequest())
    }
}