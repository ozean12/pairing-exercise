package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures.bbcAddressFixture
import io.billie.functional.data.Fixtures.bbcContactFixture
import io.billie.functional.data.Fixtures.bbcFixture
import io.billie.functional.data.Fixtures.orgRequestJson
import io.billie.functional.data.Fixtures.orgRequestJsonAddressCityInvalid
import io.billie.functional.data.Fixtures.orgRequestJsonAddressCityIsBlank
import io.billie.functional.data.Fixtures.orgRequestJsonAddressCountryCodeIsBlank
import io.billie.functional.data.Fixtures.orgRequestJsonAddressLine1IsBlank
import io.billie.functional.data.Fixtures.orgRequestJsonAddressLine2IsBlank
import io.billie.functional.data.Fixtures.orgRequestJsonAddressLine2IsMissing
import io.billie.functional.data.Fixtures.orgRequestJsonAddressMissing
import io.billie.functional.data.Fixtures.orgRequestJsonAddressWithNoCountryCode
import io.billie.functional.data.Fixtures.orgRequestJsonAddressZipCodeIsBlank
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeBlank
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeIncorrect
import io.billie.functional.data.Fixtures.orgRequestJsonNameBlank
import io.billie.functional.data.Fixtures.orgRequestJsonNoContactDetails
import io.billie.functional.data.Fixtures.orgRequestJsonNoLegalEntityType
import io.billie.functional.data.Fixtures.orgRequestJsonNoName
import io.billie.organisations.viewmodel.Entity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID
import java.util.stream.Stream


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
@TestConstructor(autowireMode = AutowireMode.ALL)
class CanStoreAndReadOrganisationTest(
    private val mockMvc: MockMvc,
    private val mapper: ObjectMapper,
    private val template: JdbcTemplate,
) {
    @Test
    fun orgs() {
        mockMvc.perform(
            get("/organisations").contentType(APPLICATION_JSON)
        ).andExpect(status().isOk())
    }

    @ParameterizedTest(name = "cannot store org when {0}")
    @MethodSource("cannotStoreOrgWhenTestDataProvider")
    fun `cannot store org test cases`(testCase: String, request: String) {
        mockMvc
            .perform(post("/organisations").contentType(APPLICATION_JSON).content(request))
            .andExpect(status().isBadRequest)
    }

    @ParameterizedTest(name = "can store org when {0}")
    @MethodSource("canStoreOrgWhenTestDataProvider")
    fun `can store org`(testCase: String, request: String, useLine2: Boolean) {
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
        val addressDetails: Map<String, Any> = addressFromDatabase(addressId)
        assertDataMatches(addressDetails, bbcAddressFixture(addressId, useLine2))
    }


    fun assertDataMatches(reply: Map<String, Any>, assertions: Map<String, Any>) {
        for (key in assertions.keys) {
            assertThat(reply[key], equalTo(assertions[key]))
        }
    }

    private fun queryEntityFromDatabase(sql: String, id: UUID): MutableMap<String, Any> =
        template.queryForMap(sql, id)

    private fun orgFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase(
            // language=PostgreSQL
            "SELECT * FROM organisations_schema.organisations WHERE id = ?",
            id
        )

    private fun contactDetailsFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase(
            // language=PostgreSQL
            "SELECT * FROM organisations_schema.contact_details WHERE id = ?",
            id
        )

    private fun addressFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase(
            // language=PostgreSQL
            "SELECT * FROM organisations_schema.addresses WHERE id = ?",
            id
        )

    companion object {
        @JvmStatic
        fun cannotStoreOrgWhenTestDataProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of("name is blank", orgRequestJsonNameBlank()),
                Arguments.of("name is missing", orgRequestJsonNoName()),
                Arguments.of("country code is missing", orgRequestJsonAddressWithNoCountryCode()),
                Arguments.of("country code is blank", orgRequestJsonCountryCodeBlank()),
                Arguments.of("country code is not recognised", orgRequestJsonCountryCodeIncorrect()),
                Arguments.of("no legal entity type", orgRequestJsonNoLegalEntityType()),
                Arguments.of("no contact details", orgRequestJsonNoContactDetails()),
                Arguments.of("address city invalid", orgRequestJsonAddressCityInvalid()),
                Arguments.of("no address", orgRequestJsonAddressMissing()),
                Arguments.of("address line 1 is blank", orgRequestJsonAddressLine1IsBlank()),
                Arguments.of("address zip code is blank", orgRequestJsonAddressZipCodeIsBlank()),
                Arguments.of("address city is blank", orgRequestJsonAddressCityIsBlank()),
                Arguments.of("address country code is blank", orgRequestJsonAddressCountryCodeIsBlank()),
            )

        @JvmStatic
        fun canStoreOrgWhenTestDataProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of("full valid request", orgRequestJson(), true),
                Arguments.of("address line 2 is blank", orgRequestJsonAddressLine2IsBlank(), false),
                Arguments.of("address line 2 is missing", orgRequestJsonAddressLine2IsMissing(), false),
            )
    }
}
