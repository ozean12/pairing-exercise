package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures.bbcAddressFixture
import io.billie.functional.data.Fixtures.bbcContactFixture
import io.billie.functional.data.Fixtures.bbcFixture
import io.billie.functional.data.Fixtures.orgRequestJson
import io.billie.functional.data.Fixtures.orgRequestJsonAddressCityBlank
import io.billie.functional.data.Fixtures.orgRequestJsonAddressCityIncorrect
import io.billie.functional.data.Fixtures.orgRequestJsonAddressCityNotBelongingToCountry
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeBlank
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeIncorrect
import io.billie.functional.data.Fixtures.orgRequestJsonNoName
import io.billie.functional.data.Fixtures.orgRequestJsonNameBlank
import io.billie.functional.data.Fixtures.orgRequestJsonNoAddress
import io.billie.functional.data.Fixtures.orgRequestJsonNoAddressPostalCode
import io.billie.functional.data.Fixtures.orgRequestJsonNoAddressStreet
import io.billie.functional.data.Fixtures.orgRequestJsonNoContactDetails
import io.billie.functional.data.Fixtures.orgRequestJsonNoCountryCode
import io.billie.functional.data.Fixtures.orgRequestJsonNoLegalEntityType
import io.billie.functional.data.Fixtures.orgResponseJson
import io.billie.organisations.viewmodel.Entity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import java.util.stream.Stream


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CanStoreAndReadOrganisationTest {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate
    @BeforeEach
    fun cleanOrgs() {
        template.execute("delete from organisations_schema.organisations")
        template.execute("delete from organisations_schema.contact_details")
        template.execute("delete from organisations_schema.addresses")
    }
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

    @ParameterizedTest
    @MethodSource("invalidAddressPayloads")
    fun cannotStoreOrgWhenInvalidAddressInput(invalidPayload: String) {
        mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(invalidPayload)
        )
            .andExpect(status().isBadRequest)
    }

    fun invalidAddressPayloads(): Stream<Arguments> = Stream.of(
        Arguments.of(orgRequestJsonNoAddress()),
        Arguments.of(orgRequestJsonCountryCodeBlank()),
        Arguments.of(orgRequestJsonCountryCodeIncorrect()),
        Arguments.of(orgRequestJsonNoCountryCode()),
        Arguments.of(orgRequestJsonNoAddressStreet()),
        Arguments.of(orgRequestJsonNoAddressPostalCode()),
        Arguments.of(orgRequestJsonAddressCityIncorrect()),
        Arguments.of(orgRequestJsonAddressCityNotBelongingToCountry()),
        Arguments.of(orgRequestJsonAddressCityBlank()),
    )

    @Test
    fun canAndRetrieveStoreOrg() {
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

        val addressId: UUID = UUID.fromString(org["address_id"] as String)
        val address = addressFromDatabase(addressId)
        assertDataMatches(address, bbcAddressFixture(addressId))

        val countryId = countryIdFromDatabase(org["country_code"] as String)

        mockMvc.perform(
            get("/organisations")
                .contentType(APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpectAll(
                MockMvcResultMatchers.content()
                    .json(
                        orgResponseJson(
                            response.id.toString(),
                            countryId,
                            contactDetailsId.toString(),
                            addressId.toString()
                        )
                    )
            )
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

    private fun countryIdFromDatabase(countryCode: String): String =
         template.queryForObject("select id from organisations_schema.countries where country_code = ?", String::class.java, countryCode)

    private fun contactDetailsFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase("select * from organisations_schema.contact_details where id = ?", id)

    private fun addressFromDatabase(id: UUID): Map<String, Any> =
        queryEntityFromDatabase("select * from organisations_schema.addresses where id = ?", id)

}
