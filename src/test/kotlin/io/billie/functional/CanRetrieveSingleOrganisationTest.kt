package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.billie.organisations.viewmodel.OrganisationResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.assertj.core.api.Assertions.*

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class CanRetrieveSingleOrganisationTest {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun `Can retrieve single organisation`() {
        val result = mockMvc.perform(
            get("/organisations")
        )
        .andExpect(status().isOk)
        .andReturn()

        val organisationList = mapper.readValue<List<OrganisationResponse>>(result.response.contentAsString)

        val firstOrganisation = organisationList[0]

        val getSingleOrganisationResult = mockMvc.perform(
            get("/organisations/${firstOrganisation.id}")
        )
            .andExpect(status().isOk)
            .andReturn()

        val getSingleOrganisationResponse = mapper.readValue<OrganisationResponse>(getSingleOrganisationResult.response.contentAsString)

        assertThat(getSingleOrganisationResponse == firstOrganisation).isTrue()
    }
}
