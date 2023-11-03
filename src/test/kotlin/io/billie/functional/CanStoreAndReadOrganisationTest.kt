package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures.bbcAddressFixture
import io.billie.functional.data.Fixtures.bbcContactFixture
import io.billie.functional.data.Fixtures.bbcFixture
import io.billie.functional.data.Fixtures.orgRequestAddressCityBlank
import io.billie.functional.data.Fixtures.orgRequestAddressCityIsIncorrect
import io.billie.functional.data.Fixtures.orgRequestAddressCountryCodeBlank
import io.billie.functional.data.Fixtures.orgRequestAddressCountryCodeIsIncorrect
import io.billie.functional.data.Fixtures.orgRequestAddressHouseNumberBlank
import io.billie.functional.data.Fixtures.orgRequestAddressNoCity
import io.billie.functional.data.Fixtures.orgRequestAddressNoCountryCode
import io.billie.functional.data.Fixtures.orgRequestAddressNoHouseNumber
import io.billie.functional.data.Fixtures.orgRequestAddressNoPostalCode
import io.billie.functional.data.Fixtures.orgRequestAddressNoStreet
import io.billie.functional.data.Fixtures.orgRequestAddressPostalCodeBlank
import io.billie.functional.data.Fixtures.orgRequestAddressStreetBlank
import io.billie.functional.data.Fixtures.orgRequestJson
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeBlank
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeIncorrect
import io.billie.functional.data.Fixtures.orgRequestJsonNameBlank
import io.billie.functional.data.Fixtures.orgRequestJsonNoContactDetails
import io.billie.functional.data.Fixtures.orgRequestJsonNoCountryCode
import io.billie.functional.data.Fixtures.orgRequestJsonNoLegalEntityType
import io.billie.functional.data.Fixtures.orgRequestJsonNoName
import io.billie.functional.data.Fixtures.orgRequestNoAddress
import io.billie.organisations.viewmodel.Entity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
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
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class CanStoreAndReadOrganisationTest {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

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
    fun cannotStoreOrgWhenNoAddressGiven() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestNoAddress())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressCountryCodeIsMissing() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressNoCountryCode())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressCountryCodeIsBlank() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressCountryCodeBlank())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressCountryCodeIsIncorrect() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressCountryCodeIsIncorrect())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressCityIsIncorrect() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressCityIsIncorrect())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressCityIsMissing() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressNoCity())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressCityIsBlank() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressCityBlank())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressStreetIsMissing() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressNoStreet())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressStreetIsBlank() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressStreetBlank())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressHouseNumberIsMissing() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressNoHouseNumber())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressHouseNumberIsBlank() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressHouseNumberBlank())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressPostalCodeIsMissing() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressNoPostalCode())
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun cannotStoreOrgWhenAddressPostalCodeIsBlank() {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestAddressPostalCodeBlank())
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

        val addressId: UUID = org["address_id"] as UUID
        val address: Map<String, Any> = addressFromDatabase(addressId)
        assertDataMatches(address, bbcAddressFixture(addressId))
    }

    fun assertDataMatches(reply: Map<String, Any>, assertions: Map<String, Any>) {
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
        queryEntityFromDatabase("select a.*, ct.country_code as address_country_code, ct.name as address_city " +
            "from organisations_schema.addresses a " +
            "INNER JOIN organisations_schema.cities ct on a.city_id::uuid = ct.id::uuid  where a.id = ?", id)

}
