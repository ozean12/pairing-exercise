package io.billie.functional

import io.billie.functional.data.Fixtures.orderRequestCreateOrder
import io.billie.functional.data.Fixtures.orderRequestCreateOrderWithoutCreatedTime
import io.billie.functional.data.Fixtures.orderRequestCreateOrderWithoutOrganisationId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class CanCreateOrdersTest {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val ORGANISATION_ID = "a5bd8429-7d48-4219-8cb0-5941c8f8821e"
    private val VALID_TIME = "2023-06-19T09:34:57"

    @Test
    fun createOrder() {

        val content = orderRequestCreateOrder(UUID.randomUUID().toString(), ORGANISATION_ID, VALID_TIME)
        mockMvc.perform(
                post("/orders").contentType(APPLICATION_JSON).content(content)
        )
                .andExpect(status().isOk)
    }

    @Test
    fun cannotCreateOrderWhenExternalIdIsBlank() {
        val content = orderRequestCreateOrder("", ORGANISATION_ID, VALID_TIME)
        mockMvc.perform(
                post("/orders").contentType(APPLICATION_JSON).content(content)
        )
                .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotCreateOrderWithSameExternalIdAndOrganisation() {
        val content = orderRequestCreateOrder(UUID.randomUUID().toString(), ORGANISATION_ID, VALID_TIME)
        mockMvc.perform(
                post("/orders").contentType(APPLICATION_JSON).content(content)
        )
                .andExpect(status().isOk)

        mockMvc.perform(
                post("/orders").contentType(APPLICATION_JSON).content(content)
        )
                .andExpect(status().isConflict)
    }

    @Test
    fun cannotCreateOrderWithoutCreatedTime() {
        val content = orderRequestCreateOrderWithoutCreatedTime(UUID.randomUUID().toString(), ORGANISATION_ID)
        mockMvc.perform(
                post("/orders").contentType(APPLICATION_JSON).content(content)
        )
                .andExpect(status().isBadRequest)
    }


    @Test
    fun cannotCreateOrderWithoutOrganisationId() {
        val content = orderRequestCreateOrderWithoutOrganisationId(UUID.randomUUID().toString(), VALID_TIME)
        mockMvc.perform(
                post("/orders").contentType(APPLICATION_JSON).content(content)
        )
                .andExpect(status().isBadRequest)
    }


}
