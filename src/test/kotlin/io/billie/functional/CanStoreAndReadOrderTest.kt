package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.billie.functional.data.Fixtures
import io.billie.functional.data.OrderFixtures
import io.billie.organisations.orders.domain.OrderId
import io.billie.organisations.orders.view.OrderDto
import io.billie.organisations.viewmodel.Entity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CanStoreAndReadOrderTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun getOrdersSuccessfullyWorksDespiteNoOrderExist() {
        // given an organisation exists
        val organisationId = createOrganisation()

        // expect successful response when orders searched by organisation
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/organisations/${organisationId.id}/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    fun getOrdersByNonExistingOrganisationFails() {
        // expect bad request error response when orders searched by organisation
        mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/organisations/${UUID.randomUUID()}/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    @Test
    fun orderCreationFailsForNonExistingOrganisation() {
        // expect bad request error response when orders searched by organisation
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/organisations/${UUID.randomUUID()}/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OrderFixtures.orderRequestJson())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun orderCreationFailsForMissingAmount() {
        // given an organisation exists
        val organisationId = createOrganisation()

        // expect bad request error response when amount is missing
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/organisations/${organisationId.id}/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OrderFixtures.orderRequestJsonMissingAmount())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun orderCreationFailsForInvalidAmount() {
        // given an organisation exists
        val organisationId = createOrganisation()

        // expect bad request error response when amount is invalid
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/organisations/${organisationId.id}/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OrderFixtures.orderRequestJsonInvalidAmount())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun canCreateAndFetchOrderWithoutShipment() {
        // given an organisation exists
        val organisationId = createOrganisation()

        // when order is created
        val orderId = createOrder(organisationId)

        // then order id is generated
        Assertions.assertNotNull(orderId)

        // when order is found by order id
        val order = getOrderById(orderId)

        // then order saved with expected values
        Assertions.assertNotNull(order)
        Assertions.assertEquals(order.organisationId, organisationId.id)
        Assertions.assertEquals(20.0, order.amount.toDouble())
    }

    @Test
    fun canCreateAndFetchMultipleOrdersWithoutShipment() {
        // given an organisation exists
        val organisationId = createOrganisation()

        // and multiple orders are created
        createOrder(organisationId)
        createOrder(organisationId)
        createOrder(organisationId)

        // when order is found by organisation id
        val ordersResponse = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/organisations/${organisationId.id}/orders")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()

        val orders: List<OrderDto> = jacksonObjectMapper().readValue(ordersResponse.response.contentAsString)

        // then order saved with expected values
        Assertions.assertEquals(3, orders.size)
    }

    private fun createOrganisation(): Entity {
        val orgResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/organisations").contentType(MediaType.APPLICATION_JSON).content(Fixtures.orgRequestJson())
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        return mapper.readValue(orgResponse.response.contentAsString, Entity::class.java)
    }

    private fun createOrder(organisationId: Entity): OrderId {
        val orderResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/organisations/${organisationId.id}/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OrderFixtures.orderRequestJson())
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        return mapper.readValue(orderResponse.response.contentAsString, OrderId::class.java)
    }

    private fun getOrderById(orderId: OrderId): OrderDto {
        val orderResponse =  mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/orders/${orderId.id}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()

        return mapper.readValue(orderResponse.response.contentAsString, OrderDto::class.java)
    }
}