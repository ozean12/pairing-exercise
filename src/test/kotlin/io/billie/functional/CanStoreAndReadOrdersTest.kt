package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures
import io.billie.functional.data.OrderFixtures
import io.billie.invoices.dto.response.OrderResponse
import io.billie.invoices.dto.response.SimpleSuccessResponse
import io.billie.invoices.resource.OrderResource
import io.billie.organisations.viewmodel.Entity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

/**
 * Group of tests for checking orders API endpoints behavior
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CanStoreAndReadOrdersTest {

    @LocalServerPort
    private var port = 0

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

    @AfterEach
    fun cleanTablesBeforeTest() {
        cleanAll()
    }

    private fun cleanAll() {
        template.execute("DELETE FROM organisations_schema.orders")
        template.execute("DELETE FROM organisations_schema.organisations")
    }

    @Test
    fun `read all request on empty DB must return empty list + OK`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/orders")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json("[]"))
    }

    @Test
    fun `should return 400 on wrong request format (not all fields set)`() {
        mockMvc
            .perform(orderPostJsonRequest(OrderFixtures.notValidOrder()))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return 400 on wrong request format (too many fields)`() {
        mockMvc
            .perform(orderPostJsonRequest(OrderFixtures.simpleOrderRequestWithExtraField()))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return 409 code in case of no organisation is saved in the system for the order`() {
        val organisationId = UUID.randomUUID()
        val requestWithNotKnownOrg = OrderFixtures.simpleValidOrderRequestWithOrganisation(organisationId)
        val errorResponse = mockMvc
            .perform(orderPostJsonRequest(requestWithNotKnownOrg))
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andReturn()

        val response = errorResponse.response.errorMessage
        assertTrue(response != null)
        assertTrue(response!!.contains(organisationId.toString()))
    }

    @Test
    fun `should return 409 in case of order with the same ID already exists`() {
        // load organisation for test, so order can be saved
        val organisationUUID = loadOrganisationForTest()
        val orderRequest = OrderFixtures.simpleValidOrderRequestWithOrganisation(organisationUUID)

        // send order for the first time
        mockMvc
            .perform(orderPostJsonRequest(orderRequest))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // send second time and get an error
        mockMvc
            .perform(orderPostJsonRequest(orderRequest))
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }


    @Test
    fun `should store valid order successfully with appropriate message in case order's organisation is registered`() {
        // load organisation for test, so order can be saved
        val organisationUUID = loadOrganisationForTest()
        val orderIdForTest = UUID.randomUUID()
        val orderRequest = OrderFixtures.simpleValidOrderRequest(orderIdForTest, organisationUUID)

        val orderCreatingResponse = mockMvc
            .perform(orderPostJsonRequest(orderRequest))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val response = mapper.readValue(orderCreatingResponse.response.contentAsString, SimpleSuccessResponse::class.java)
        val expectedResponseMessage = OrderResource.OrderResource.successOrderCreationMsg(orderIdForTest)

        assertEquals(response.comment, expectedResponseMessage)
    }

    @Test
    fun `should return order on get request if it has been stored correctly`() {
        val organisationUUID = loadOrganisationForTest()
        val orderIdForTest = UUID.randomUUID()
        val orderRequest = OrderFixtures.simpleValidOrderRequest(orderIdForTest, organisationUUID)
        // store order
        mockMvc
            .perform(orderPostJsonRequest(orderRequest))
            .andExpect(MockMvcResultMatchers.status().isOk)

        val orders = mockMvc
            .perform(MockMvcRequestBuilders.get("/orders"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val response = mapper.readValue(orders.response.contentAsString, Array<OrderResponse>::class.java)
        assertEquals(response.size, 1)
        val storedOrder = response.first()
        assertEquals(storedOrder.orderId, orderIdForTest)
        assertEquals(storedOrder.organisationId, organisationUUID)
    }

    @Test
    fun `should return multiple orders on get request if they have been stored correctly`() {
        val organisationUUID = loadOrganisationForTest()

        val orderIdForTest1 = UUID.randomUUID()
        val orderRequest1 = OrderFixtures.simpleValidOrderRequest(orderIdForTest1, organisationUUID)

        val orderIdForTest2 = UUID.randomUUID()
        val orderRequest2 = OrderFixtures.simpleValidOrderRequest(orderIdForTest2, organisationUUID)

        // store 2 orders
        mockMvc
            .perform(orderPostJsonRequest(orderRequest1))
            .andExpect(MockMvcResultMatchers.status().isOk)
        mockMvc
            .perform(orderPostJsonRequest(orderRequest2))
            .andExpect(MockMvcResultMatchers.status().isOk)

        val orders = mockMvc
            .perform(MockMvcRequestBuilders.get("/orders"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // check that we have full size list
        val response = mapper.readValue(orders.response.contentAsString, Array<OrderResponse>::class.java)
        assertEquals(response.size, 2)
        // check content of the list
        val firstOrder = response[0]
        assertEquals(firstOrder.orderId, orderIdForTest1)
        assertEquals(firstOrder.organisationId, organisationUUID)
        val secondOrder = response[1]
        assertEquals(secondOrder.orderId, orderIdForTest2)
        assertEquals(secondOrder.organisationId, organisationUUID)

    }

    private fun orderPostJsonRequest(content: String) =
        MockMvcRequestBuilders
            .post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)

    private fun loadOrganisationForTest(): UUID {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/organisations")
                .contentType(MediaType.APPLICATION_JSON).content(Fixtures.orgRequestJson())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)
        return response.id
    }
}