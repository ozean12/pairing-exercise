package io.billie.organization_management

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.organization_management.data.Fixtures.orgRequestJson
import io.billie.organization_management.data.Fixtures.orgRequestJsonCountryCodeBlank
import io.billie.organization_management.data.Fixtures.orgRequestJsonCountryCodeIncorrect
import io.billie.organization_management.data.Fixtures.orgRequestJsonNameBlank
import io.billie.organization_management.data.Fixtures.orgRequestJsonNoContactDetails
import io.billie.organization_management.data.Fixtures.orgRequestJsonNoCountryCode
import io.billie.organization_management.data.Fixtures.orgRequestJsonNoLegalEntityType
import io.billie.organization_management.data.Fixtures.orgRequestJsonNoName
import io.billie.organization_management.organisations.data.OrganisationRepository
import io.billie.organization_management.organisations.model.Entity
import io.billie.organization_management.organisations.model.OrganisationRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
class CanStoreAndReadOrganisationTest {

    companion object {

        @Container
        @ServiceConnection
        val postgreSQLContainer = PostgreSQLContainer("postgres:latest")
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var organisationRepository: OrganisationRepository

    @Test
    fun `should return 200 when getting all organisations`() {
        mockMvc.perform(
            get("/organisations")
                .contentType(APPLICATION_JSON),
        )
            .andExpect(status().isOk())
    }

    @Test
    fun `should return 400 when name field of request body is blank`() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNameBlank()),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when name field of request body is missing`() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNoName()),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when country_code field of request body is missing`() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNoCountryCode()),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when country_code field of request body is blank`() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonCountryCodeBlank()),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when country_code field of request body is unrecognizable`() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonCountryCodeIncorrect()),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when legal_entity_type field of request body is missing`() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNoLegalEntityType()),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when contact_detail field of request body is missing`() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNoContactDetails()),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 200 when request body is well-defined`() {
        val orgRequestJson = orgRequestJson()
        val result = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJson),
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = objectMapper.readValue(result.response.contentAsString, Entity::class.java)
        val organisation = organisationRepository.findById(response.id).get()
        val organisationRequest = objectMapper.readValue(orgRequestJson, OrganisationRequest::class.java)

        assertAll {
            assertThat(organisation::id).isNotNull()
            assertThat(organisation.id!!::class.java).isEqualTo(UUID::class.java)
            assertThat(organisation::name).isEqualTo(organisationRequest.name)
            assertThat(organisation::dateFounded).isEqualTo(organisationRequest.dateFounded)
            assertThat(organisation.country::code).isEqualTo(organisationRequest.countryCode)
            assertThat(organisation::vatNumber).isEqualTo(organisationRequest.vatNumber)
            assertThat(organisation::legalEntityType).isEqualTo(organisationRequest.legalEntityType)
            assertThat(organisation.contactDetail::id).isNotNull()
            assertThat(organisation.contactDetail.id!!::class.java).isEqualTo(UUID::class.java)
            assertThat(organisation.contactDetail::phoneNumber).isEqualTo(organisationRequest.contactDetail.phoneNumber)
            assertThat(organisation.contactDetail::email).isEqualTo(organisationRequest.contactDetail.email)
            assertThat(organisation.contactDetail::fax).isEqualTo(organisationRequest.contactDetail.fax)
        }
    }
}
