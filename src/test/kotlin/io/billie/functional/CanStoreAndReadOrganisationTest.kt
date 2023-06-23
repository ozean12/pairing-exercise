package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures
import io.billie.functional.data.Fixtures.bbcContactFixture
import io.billie.functional.data.Fixtures.bbcFixture
import io.billie.functional.data.Fixtures.orgRequestJson
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeBlank
import io.billie.functional.data.Fixtures.orgRequestJsonCountryCodeIncorrect
import io.billie.functional.data.Fixtures.orgRequestJsonNameBlank
import io.billie.functional.data.Fixtures.orgRequestJsonNoContactDetails
import io.billie.functional.data.Fixtures.orgRequestJsonNoCountryCode
import io.billie.functional.data.Fixtures.orgRequestJsonNoLegalEntityType
import io.billie.functional.data.Fixtures.orgRequestJsonNoName
import io.billie.functional.utils.ClearDatabaseBeforeEach
import io.billie.organisations.viewmodel.Entity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import java.util.stream.Stream


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(ClearDatabaseBeforeEach::class)
class CanStoreAndReadOrganisationTest {
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

    // address
    @ParameterizedTest
    @MethodSource("validOrgAddressRequestPayloads")
    fun canStoreOrgAddress(caseIdentifier: String, payload: String) {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/organisations/addresses")
                .contentType(MediaType.APPLICATION_JSON).content(payload)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)

        val org: Map<String, Any> = addressFromDatabase(response.id)
        assertDataMatches(org, Fixtures.orgAddressFixture(response.id))
    }

    @ParameterizedTest
    @MethodSource("invalidOrgAddressRequestPayloads")
    fun cannotStoreOrgAddressForInvalidPayload(caseIdentifier: String, payload: String) {
        mockMvc.perform(
            post("/organisations/addresses").contentType(APPLICATION_JSON).content(payload)
        )
            .andExpect(status().isBadRequest)
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

    private fun addressFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase("select * from organisations_schema.addresses where id = ?", id)

    private fun contactDetailsFromDatabase(id: UUID): MutableMap<String, Any> =
        queryEntityFromDatabase("select * from organisations_schema.contact_details where id = ?", id)

    companion object {
        @JvmStatic
        fun invalidOrgAddressRequestPayloads(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "missing org id",
                    """
                        {
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "plot_number": "45",
                            "floor": "3"
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "missing city id",
                    """
                        {
                            "organisation_id": "fa55c095-c771-4901-bb87-1624ac7c1eeb",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "plot_number": "45",
                            "floor": "3"
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "missing pin code",
                    """
                        {
                            "organisation_id": "fa55c095-c771-4901-bb87-1624ac7c1eeb",
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "street_name": "Metzer Strasse",
                            "plot_number": "45",
                            "floor": "3"
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "missing street name",
                    """
                        {
                            "organisation_id": "fa55c095-c771-4901-bb87-1624ac7c1eeb",
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "plot_number": "45",
                            "floor": "3"
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "missing plot number",
                    """
                        {
                            "organisation_id": "fa55c095-c771-4901-bb87-1624ac7c1eeb",
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "floor": "3"
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "null org id",
                    """
                        {
                            "organisation_id": null,
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "plot_number": "45",
                            "floor": "3"
                        }
                    """.trimIndent()
                ),
            )
        }

        @JvmStatic
        fun validOrgAddressRequestPayloads(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "full payload",
                    """
                        {
                            "organisation_id": "fa55c095-c771-4901-bb87-1624ac7c1eeb",
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "plot_number": "45",
                            "floor": "3",
                            "apartment_number": "13"
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "missing floor",
                    """
                        {
                            "organisation_id": "fa55c095-c771-4901-bb87-1624ac7c1eeb",
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "plot_number": "45",
                            "apartment_number": "13"
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "missing apartment number",
                    """
                        {
                            "organisation_id": "fa55c095-c771-4901-bb87-1624ac7c1eeb",
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "plot_number": "45",
                            "floor": "3"
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "int plot, floor and apartmentNumber",
                    """
                        {
                            "organisation_id": "fa55c095-c771-4901-bb87-1624ac7c1eeb",
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "plot_number": 45,
                            "floor": 3,
                            "apartment_number": 13
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "null floor and apartmentNumber",
                    """
                        {
                            "organisation_id": "fa55c095-c771-4901-bb87-1624ac7c1eeb",
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "plot_number": "45",
                            "floor": null,
                            "apartment_number": null
                        }
                    """.trimIndent()
                ),
            )
        }
    }

}
