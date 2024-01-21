package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures
import io.billie.functional.data.OrderFixtures
import io.billie.functional.data.ShipmentFixtures
import io.billie.organisations.orders.domain.OrderId
import io.billie.organisations.orders.domain.ShipmentAmount
import io.billie.organisations.orders.domain.ShipmentId
import io.billie.organisations.viewmodel.Entity
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CanStoreShipmentTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

    @Test
    fun shipmentNotificationFailsForNonExistingOrder() {
        // expect bad request error response when orders searched by organisation
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/orders/${UUID.randomUUID()}/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OrderFixtures.orderRequestJson())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun shipmentNotificationFailsForMissingAmount() {
        // given an organisation exists
        val organisationId = createOrganisation()
        // and order exists
        val orderId = createOrder(organisationId)

        // expect bad request error response when amount is missing
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/orders/${orderId.id}/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ShipmentFixtures.shipmentRequestJsonMissingAmount())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun shipmentNotificationFailsForInvalidAmount() {
        // given an organisation exists
        val organisationId = createOrganisation()
        // and order exists
        val orderId = createOrder(organisationId)

        // expect bad request error response when amount is missing
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/orders/${orderId.id}/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ShipmentFixtures.shipmentRequestJsonInvalidAmount())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun canNotifyShipment() {
        // given an organisation exists
        val organisationId = createOrganisation()
        // and order exists
        val orderId = createOrder(organisationId)

        // when shipment is notified for an order
        val shipmentId = notifyShipment(orderId, ShipmentAmount(BigDecimal.TEN))

        // then shipment saved
        MatcherAssert.assertThat(shipmentId, notNullValue())
    }

    @Test
    fun canNotNotifyShipmentIfAmountExceedsOrderAmount() {
        // given an organisation exists
        val organisationId = createOrganisation()
        // and order exists
        val orderId = createOrder(organisationId)

        // when 1st shipment is notified successfully - total amount 10
        notifyShipment(orderId, ShipmentAmount(BigDecimal.TEN))

        // when 2nd shipment is notified successfully - total amount 20
        notifyShipment(orderId, ShipmentAmount(BigDecimal.TEN))

        // expect bad request error response when amount exceeds order amount 20
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/orders/${orderId.id}/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ShipmentFixtures.shipmentRequestJson(ShipmentAmount(BigDecimal.TEN)))
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
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

    private fun notifyShipment(orderId: OrderId, shipmentAmount: ShipmentAmount) : ShipmentId {
        val shipmentResponse = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/orders/${orderId.id}/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ShipmentFixtures.shipmentRequestJson(shipmentAmount))
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        return mapper.readValue(shipmentResponse.response.contentAsString, ShipmentId::class.java)
    }

    private fun queryEntityFromDatabase(sql: String, shipmentId: ShipmentId): MutableMap<String, Any> =
            template.queryForMap(sql, shipmentId.id)
}