package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.billie.functional.data.Fixtures
import io.billie.functional.data.InvoiceFixtures
import io.billie.functional.data.OrderFixtures
import io.billie.invoices.dto.response.FullInvoiceResponse
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
 * Group of tests for checking invoices endpoints behavior
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CanStoreAndReadInvoicesTest {

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
        template.execute("DELETE FROM organisations_schema.invoices")
        template.execute("DELETE FROM organisations_schema.orders")
        template.execute("DELETE FROM organisations_schema.organisations")
    }

    @Test
    fun `read all request on empty DB must return empty list + OK`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/invoices")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json("[]"))
    }

    @Test
    fun `should return 400 on wrong request format (wrong fields)`() {
        mockMvc
            .perform(prepareInvoicePostJsonRequest(InvoiceFixtures.notValidInvoiceRequest()))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return 400 on wrong request format (extra fields)`() {
        mockMvc
            .perform(prepareInvoicePostJsonRequest(InvoiceFixtures.validInvoiceRequestWithExtra()))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return 400 on order_id that is not stored in db`() {
        val orderIdForTest = UUID.randomUUID()
        val response = mockMvc
            .perform(prepareInvoicePostJsonRequest(InvoiceFixtures.validInvoiceRequest(orderIdForTest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()

        val errMsg = response.response.errorMessage
        assertTrue(errMsg != null)
        assertTrue(errMsg!!.contains(orderIdForTest.toString()))
    }

    @Test
    fun `should store invoice correctly if organisation and order are in DB`() {
        // load organisation and order
        val organisationId = loadOrganisationForTest()
        val orderId = loadOrderForTest(organisationId)
        // create invoice for order
        val invoice = mockMvc
            .perform(prepareInvoicePostJsonRequest(InvoiceFixtures.validInvoiceRequest(orderId)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val storedInvoiceJson = invoice.response.contentAsString
        val storedInvoice = mapper.readValue(storedInvoiceJson, FullInvoiceResponse::class.java)
        // brief check
        assertEquals(storedInvoice.orderId, orderId)
        assertEquals(storedInvoice.organisation.name, "BBC")
        // in-depth check excluding time and id
        val gson = Gson()
        val invoiceAsJsObj = gson.fromJson(storedInvoiceJson, JsonObject::class.java)
        invoiceAsJsObj.remove("id")
        invoiceAsJsObj.remove("issue_date")

        val expectedJsObj = gson.fromJson(expectedJsonWithoutIdAndTime(orderId), JsonObject::class.java)
        assertEquals(invoiceAsJsObj, expectedJsObj)
    }

    @Test
    fun `in case invoice for such order_id exists return it instead of creating new one`() {
        // load organisation and order
        val organisationId = loadOrganisationForTest()
        val orderId = loadOrderForTest(organisationId)
        // create invoice for order
        val invoice = mockMvc
            .perform(prepareInvoicePostJsonRequest(InvoiceFixtures.validInvoiceRequest(orderId)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val storedInvoice = mapper.readValue(invoice.response.contentAsString, FullInvoiceResponse::class.java)
        val initInvoiceId = storedInvoice.id

        // trying to send one more invoice request with the same order_id
        val newInvoice = mockMvc
            .perform(prepareInvoicePostJsonRequest(InvoiceFixtures.validInvoiceRequest(orderId)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val newInvoiceId = mapper.readValue(newInvoice.response.contentAsString, FullInvoiceResponse::class.java).id
        // check that invoice id hasn't changed
        assertEquals(initInvoiceId, newInvoiceId)
    }

    private fun prepareInvoicePostJsonRequest(content: String) =
        MockMvcRequestBuilders
            .post("/invoices/prepare")
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

    private fun loadOrderForTest(organisationId: UUID): UUID {
        val orderUUID = UUID.randomUUID()
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OrderFixtures.simpleValidOrderRequest(orderUUID, organisationId))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        return orderUUID
    }

    private fun expectedJsonWithoutIdAndTime(orderId: UUID) = """
        {
           "order_id":"$orderId",
           "is_paid":false,
           "organisation":{
              "name":"BBC",
              "vat_number":"333289454"
           },
           "customer":{
              "name":"CocaComany",
              "vat_number":"234252515315",
              "address":"Kurfurstendamm 237, 10719 Berlin"
           },
           "total_gross":750.0,
           "products":[
              {
                 "name":"Red Cup",
                 "price":100.0,
                 "quantity":2
              },
              {
                 "name":"Blue Cup",
                 "price":110.0,
                 "quantity":5
              }
           ]
        }
    """.trimIndent()
}