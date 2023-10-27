package io.billie.functional.orders

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.customers.data.CustomerRepository
import io.billie.functional.data.generateCustomerRequest
import io.billie.functional.data.generateOrderRequest
import io.billie.orders.data.OrderRepository
import io.billie.orders.dto.OrderRequest
import io.billie.orders.dto.OrderResponse
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.util.*
import java.util.stream.IntStream

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class OrderResourceTest {

    @Autowired
    private lateinit var mocMvc: MockMvc

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var jacksonTester: JacksonTester<OrderRequest>

    @BeforeEach
    fun resetDatabase() {
        jdbcTemplate.execute("delete from organisations_schema.customer_orders")
        jdbcTemplate.execute("delete from organisations_schema.customers")
    }

    @Test
    fun `should return empty when there is no orders in database`() {
        val findOrdersCall = mocMvc.perform(
                MockMvcRequestBuilders.get("/orders")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
                .andReturn()

        val customerResponses = objectMapper.readValue(findOrdersCall.response.contentAsString, Array<OrderResponse>::class.java)

        Assertions.assertThat(customerResponses.size).isZero
    }

    @Test
    fun `should return all orders from database`() {
        val orderAmount = BigDecimal.valueOf(100.00)
        IntStream.rangeClosed(0, 2)
                .forEach { generateOrder(orderAmount) }

        val findAllOrdersCall =
                mocMvc.perform(
                        MockMvcRequestBuilders.get("/orders")
                )
                        .andExpect(status().isOk)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn()

        val customerResponses = objectMapper.readValue(findAllOrdersCall.response.contentAsString, Array<OrderResponse>::class.java)
        Assertions.assertThat(customerResponses.size).isEqualTo(3)

        val firstOrder = customerResponses[0]
        Assertions.assertThat(firstOrder.amount.toDouble()).isEqualTo(orderAmount.toDouble())
    }

    @Test
    fun `create new order should return order uuid`() {
        val customerId = generateCustomer()
        val orderAmount = BigDecimal.valueOf(100.00)
        val orderRequest = generateOrderRequest(customerId, orderAmount)

        val createOrderCall = mocMvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(orderRequest).json)
                )
                .andExpect(status().isCreated)
                .andReturn()

        val createResult = objectMapper.readValue(createOrderCall.response.contentAsString, Entity::class.java)

        Assertions.assertThat(createResult.id).isNotNull
    }

    @Test
    fun `create new order with amount zero should return 400`() {
        val customerId = generateCustomer()
        val orderAmount = BigDecimal.valueOf(0.00)
        val orderRequest = generateOrderRequest(customerId, orderAmount)

        mocMvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(orderRequest).json)
                )
                .andExpect(status().isBadRequest)
                .andReturn()
    }

    @Test
    fun `create new order with amount negative should return 400`() {
        val customerId = generateCustomer()
        val orderAmount = BigDecimal.valueOf(-10.00)
        val orderRequest = generateOrderRequest(customerId, orderAmount)

        mocMvc.perform(
                post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(orderRequest).json)
                )
                .andExpect(status().isBadRequest)
                .andReturn()
    }

    private fun generateOrder(amount: BigDecimal): UUID {
        val customerId = generateCustomer()
        return orderRepository.createOrder(generateOrderRequest(customerId, amount))
    }

    private fun generateCustomer(): UUID =
            customerRepository.createCustomer(generateCustomerRequest())

}