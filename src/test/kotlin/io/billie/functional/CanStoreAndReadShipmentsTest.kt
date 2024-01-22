package io.billie.functional

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures.shipmentFixture
import io.billie.functional.data.Fixtures.shipmentsRequestJson
import io.billie.functional.data.Fixtures.shipmentsRequestJsonOrderIdInvalid
import io.billie.functional.data.Fixtures.shipmentsRequestJsonOrderIdMissing
import io.billie.functional.data.Fixtures.shipmentsRequestJsonShipmentAmountIsMissing
import io.billie.functional.data.Fixtures.shipmentsRequestJsonShipmentAmountNegative
import io.billie.functional.data.Fixtures.shipmentsRequestJsonWithRandomOrderId
import io.billie.orders.model.Order
import io.billie.orders.model.OrderStatus
import io.billie.shared.PostgresqlSharedContainer
import io.billie.shipments.model.Shipment
import io.billie.shipments.viewmodel.ShipmentResponse
import io.billie.util.createOrderInDatabase
import io.billie.util.createShipmentInDatabase
import io.billie.util.queryEntityFromDatabase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class CanStoreAndReadShipmentsTest : PostgresqlSharedContainer() {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

    @Test
    fun cannotStoreShipmentWhenOrderIdIsMissing() {
        mockMvc.perform(
            post("/shipments").contentType(APPLICATION_JSON).content(shipmentsRequestJsonOrderIdMissing())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreShipmentWhenOrderIdIsInvalidUUID() {
        mockMvc.perform(
            post("/shipments").contentType(APPLICATION_JSON).content(shipmentsRequestJsonOrderIdInvalid())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreShipmentWhenOrderIdIIsNotFound() {
        mockMvc.perform(
            post("/shipments").contentType(APPLICATION_JSON).content(shipmentsRequestJsonWithRandomOrderId())
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun cannotStoreShipmentWhenAmountIsMissing() {
        mockMvc.perform(
            post("/shipments").contentType(APPLICATION_JSON).content(shipmentsRequestJsonShipmentAmountIsMissing())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreShipmentWithNegativeAmount() {
        mockMvc.perform(
            post("/shipments").contentType(APPLICATION_JSON).content(shipmentsRequestJsonShipmentAmountNegative())
        )
            .andExpect(status().isBadRequest)
    }


    @Test
    fun canStoreShipment() {

        val orderId = UUID.randomUUID()
        val organizationId = UUID.randomUUID()
        val order = Order(
            id = orderId,
            organizationId = organizationId,
            totalAmount = BigDecimal("1000.00"),
            noOfTotalItems = 5,
            status = OrderStatus.PENDING
        )
        val shipmentAmount = BigDecimal("1000.00")

        template.createOrderInDatabase(order)

        val result = mockMvc.perform(
            post("/shipments").contentType(APPLICATION_JSON).content(shipmentsRequestJson(orderId, shipmentAmount))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val response = mapper.readValue(result.response.contentAsString, ShipmentResponse::class.java)

        val shipment: Map<String, Any> = shipmentFromDatabase(response.id)
        assertDataMatches(shipment, shipmentFixture(response.id, orderId, shipmentAmount))
    }

    @Test
    fun canReadShipmentsByOrderId() {

        val orderId = UUID.randomUUID()
        val organizationId = UUID.randomUUID()
        val order = Order(
            id = orderId,
            organizationId = organizationId,
            totalAmount = BigDecimal("1000.00"),
            noOfTotalItems = 5,
            status = OrderStatus.PENDING
        )
        val shipment = Shipment(UUID.randomUUID(), orderId, BigDecimal("1000.00"), LocalDateTime.now())

        template.createOrderInDatabase(order)
        template.createShipmentInDatabase(shipment)


        val result = mockMvc.perform(
            get("/shipments/order/$orderId").contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

        val response: List<ShipmentResponse> = mapper.readValue(
            result.response.contentAsString,
            object : TypeReference<List<ShipmentResponse>>() {})

        val firstShipmentFromResponse = response.first()
        assertEquals(1, response.size)
        assertEquals(orderId, firstShipmentFromResponse.orderId)

    }

    @Test
    fun getShipmentsByOrderId_returnsEmptyListWhenOrderDoesNotExists() {


        val result = mockMvc.perform(
            get("/shipments/order/${UUID.randomUUID()}").contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

        val response: List<ShipmentResponse> = mapper.readValue(
            result.response.contentAsString,
            object : TypeReference<List<ShipmentResponse>>() {})

        assertEquals(response.isEmpty(), true)

    }

    @Test
    fun canReadShipmentsByShipmentId() {

        val orderId = UUID.randomUUID()
        val organizationId = UUID.randomUUID()
        val order = Order(
            id = orderId,
            organizationId = organizationId,
            totalAmount = BigDecimal("1000.00"),
            noOfTotalItems = 5,
            status = OrderStatus.PENDING
        )
        val shipment = Shipment(UUID.randomUUID(), orderId, BigDecimal("1000.00"), LocalDateTime.now())

        template.createOrderInDatabase(order)
        template.createShipmentInDatabase(shipment)

        val result = mockMvc.perform(
            get("/shipments/${shipment.id!!}").contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = mapper.readValue(
            result.response.contentAsString,
            ShipmentResponse::class.java
        )

        assertEquals(shipment.id, response.id)
        assertEquals(shipment.orderId, response.orderId)
        assertEquals(shipment.shipmentAmount, response.shipmentAmount)
        assertEquals(shipment.shippedAt, response.shippedAt)

    }

    @Test
    fun getShipmentsByShipmentId_returns404WhenShipmentDoesNotExists() {


        mockMvc.perform(
            get("/shipments/${UUID.randomUUID()}").contentType(APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)

    }

    fun assertDataMatches(reply: Map<String, Any>, assertions: Map<String, Any>) {
        for (key in assertions.keys) {
            assertThat(reply[key], equalTo(assertions[key]))
        }
    }

    private fun shipmentFromDatabase(id: UUID): MutableMap<String, Any> =
        template.queryEntityFromDatabase("select * from shipments_schema.shipments where id = ?", id)

}
