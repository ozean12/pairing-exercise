package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.countries.data.CityRepository
import io.billie.functional.data.Fixtures
import io.billie.functional.data.Fixtures.bbcAddressFixture
import io.billie.functional.data.Fixtures.bbcContactFixture
import io.billie.functional.data.Fixtures.bbcFixture
import io.billie.functional.data.Fixtures.orgRequestJson
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeBlank
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeIncorrect
import io.billie.functional.data.Fixtures.orgRequestJsonNoName
import io.billie.functional.data.Fixtures.orgRequestJsonNameBlank
import io.billie.functional.data.Fixtures.orgRequestJsonNoContactDetails
import io.billie.functional.data.Fixtures.orgRequestJsonNoCountryCode
import io.billie.functional.data.Fixtures.orgRequestJsonNoLegalEntityType
import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.viewmodel.Entity
import io.billie.organisations.viewmodel.OrganisationRequest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import java.util.stream.Stream


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CanStoreAndReadOrganisationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

    @Autowired
    private lateinit var organisationRepository: OrganisationRepository

    @Autowired
    private lateinit var cityRepository : CityRepository

    @Test
    fun orgs() {
        mockMvc.perform(
            get("/organisations")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
    }

    @Test
    fun cannotStoreOrgWhenNameIsBlank() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNameBlank())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenNameIsMissing() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNoName())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenCountryCodeIsMissing() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNoCountryCode())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenCountryCodeIsBlank() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonCountryCodeBlank())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenCountryCodeIsNotRecognised() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonCountryCodeIncorrect())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenNoLegalEntityType() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNoLegalEntityType())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenNoContactDetails() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJsonNoContactDetails())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun canStoreOrg() {
        val result = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJson())
        )
        .andExpect(status().isOk)
        .andReturn()

        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)

        val org: Map<String, Any> = orgFromDatabase(response.id)
        assertDataMatches(org, bbcFixture(response.id))

        val contactDetailsId: UUID = UUID.fromString(org["contact_details_id"] as String)
        val contactDetails: Map<String, Any> = contactDetailsFromDatabase(contactDetailsId)
        assertDataMatches(contactDetails, bbcContactFixture(contactDetailsId))
    }

    @Test
    fun `can add address to existing organisation`() {
        val cityId = cityRepository.findByCountryCode("NL").first().id
        // create an organisation without address
        val organisationId = organisationRepository.create(mapper.readValue(orgRequestJson(), OrganisationRequest::class.java))
        // add address
        val result = mockMvc.perform(
            post("/organisations/$organisationId/addresses").contentType(APPLICATION_JSON).content(
                Fixtures.addressRequestJson(
                    cityId.toString()
                )
            )
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)

        val org: Map<String, Any> = orgFromDatabase(organisationId)
        assertDataMatches(org, bbcFixture(organisationId))

        val addressId: UUID = response.id
        val address: Map<String, Any> = addressFromDatabase(addressId)
        assertDataMatches(address, Fixtures.bbcAddressFixture(addressId))
    }

    @Test
    fun `can store organisation with address`() {
        val cityId = cityRepository.findByCountryCode("NL").first().id
        val result = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(Fixtures.orgRequestWithAddressJson(cityId.toString()))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)

        val org: Map<String, Any> = orgFromDatabase(response.id)
        assertDataMatches(org, bbcFixture(response.id))

        val contactDetailsId: UUID = UUID.fromString(org["contact_details_id"] as String)
        val addressId: UUID = org["address_id"] as UUID
        val contactDetails: Map<String, Any> = contactDetailsFromDatabase(contactDetailsId)
        val address: Map<String, Any> = addressFromDatabase(addressId)
        assertDataMatches(contactDetails, bbcContactFixture(contactDetailsId))
        assertDataMatches(address, bbcAddressFixture(addressId))
    }


    @ParameterizedTest
    @MethodSource("validAddressRequests")
    fun `stores valid addresses`(caseIdentifier: String, payload: MutableMap<String, Any>) {
        val organisationId = organisationRepository.create(mapper.readValue(orgRequestJson(), OrganisationRequest::class.java))
        val cityId = cityRepository.findByCountryCode("NL").first().id
        payload["city_id"] = cityId
        val result = mockMvc.perform(
            post("/organisations/$organisationId/addresses").contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload))
        )
            .andExpect(status().isOk)
            .andReturn()

        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)

        val address: Map<String, Any> = addressFromDatabase(response.id)
        assertDataMatches(
            address,
            mapOf(
                "id" to response.id,
                "city_id" to cityId,
                "zip_code" to payload["zip_code"],
                "street" to payload["street"],
                "street_number" to payload["street_number"],
                "apartment_number" to payload["apartment_number"]
            )
        )
    }

    @ParameterizedTest
    @MethodSource("invalidAddressRequests")
    fun `invalid addresses return http 400`(caseIdentifier: String, payload: MutableMap<String, Any>) {
        val organisationId = UUID.randomUUID()
        mockMvc.perform(
            post("/organisations/$organisationId/addresses").contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(payload))
        )
            .andExpect(status().isBadRequest)
            .andReturn()
    }

    fun assertDataMatches(reply: Map<String, Any>, assertions: Map<String, Any?>) {
        for (key in assertions.keys) {
            assertThat(reply[key], equalTo(assertions[key]))
        }
    }

    private fun queryEntityFromDatabase(sql: String, id: UUID): MutableMap<String, Any> =
        template.queryForMap(sql, id)

    private fun orgFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase("select * from organisations_schema.organisations where id = ?", id)

    private fun contactDetailsFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase("select * from organisations_schema.contact_details where id = ?", id)

    private fun addressFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase("select * from organisations_schema.addresses where id = ?", id)

    companion object {
        private fun buildAddressRequestMap(replace: Map<String, Any?> = emptyMap()): MutableMap<String, Any?> {
            val addressRequestMap = mutableMapOf<String, Any?>(
                "city_id" to UUID.randomUUID(),
                "zip_code" to "1046AC",
                "street" to "Jarmuiden",
                "street_number" to "31",
                "apartment_number" to "4A"
            )
            addressRequestMap.putAll(replace)
            return addressRequestMap
        }
        @JvmStatic
        fun validAddressRequests(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "full address",
                    buildAddressRequestMap()
                ),
                Arguments.of(
                    "no apartmentNumber provided",
                    buildAddressRequestMap(mapOf("apartment_number" to null))
                ),
                Arguments.of(
                    "zip code max length",
                     buildAddressRequestMap(mapOf("zip_code" to "a".repeat(10)))
                ),
            )
        }
        @JvmStatic
        fun invalidAddressRequests(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "zip code too large",
                    buildAddressRequestMap(mapOf("zip_code" to "a".repeat(11)))
                ),
                Arguments.of(
                    "zip code null",
                    buildAddressRequestMap(mapOf("zip_code" to null))
                ),
                Arguments.of(
                    "streetNumber too large",
                    buildAddressRequestMap(mapOf("street_number" to "a".repeat(11)))
                ),
                Arguments.of(
                    "streetNumber null",
                    buildAddressRequestMap(mapOf("street_number" to null))
                ),
                Arguments.of(
                    "street too large",
                    buildAddressRequestMap(mapOf("street" to "a".repeat(51)))
                ),
                Arguments.of(
                    "street null",
                    buildAddressRequestMap(mapOf("street" to null))
                ),
                Arguments.of(
                    "apartmentNumber too large",
                    buildAddressRequestMap(mapOf("apartment_number" to "a".repeat(6)))
                ),
                Arguments.of(
                    "city_id is null",
                    buildAddressRequestMap(mapOf("city_id" to null))
                ),
            )
        }
    }
}
