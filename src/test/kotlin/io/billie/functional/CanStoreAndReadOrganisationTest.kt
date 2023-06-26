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

    @ParameterizedTest
    @MethodSource("invalidOrgAddressRequestPayloads")
    fun cannotStoreOrgAddressForInvalidPayload(caseIdentifier: String, payload: String) {
        // given
        val addOrgResult = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJson())
        )
            .andExpect(status().isOk)
            .andReturn()
        val orgResponse = mapper.readValue(addOrgResult.response.contentAsString, Entity::class.java)
        val city: Map<String, Any> = cityFromDatabase(countryCode = "DE", cityName = "Berlin")

        // when
        val response = mockMvc.perform(
            post("/organisations/${orgResponse.id}/addresses").contentType(APPLICATION_JSON).content(payload)
        )
            .andExpect(status().isBadRequest)

        var a = 4
    }

    // address
    @ParameterizedTest
    @MethodSource("validOrgAddressRequestPayloads")
    fun canStoreOrgAddress(caseIdentifier: String, payload: MutableMap<String, Any?>) {
        // given
        val addOrgResult = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgRequestJson())
        )
            .andExpect(status().isOk)
            .andReturn()
        val orgResponse = mapper.readValue(addOrgResult.response.contentAsString, Entity::class.java)
        val city: Map<String, Any> = cityFromDatabase(countryCode = "DE", cityName = "Berlin")

        // when
        payload["city_id"] = city["id"]
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/organisations/${orgResponse.id}/addresses")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(payload))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val response = mapper.readValue(result.response.contentAsString, Entity::class.java)

        // then
        val org: Map<String, Any> = addressFromDatabase(response.id)
        assertDataMatches(org, Fixtures.orgAddressFixture(response.id, orgResponse.id, city["id"] as UUID))
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

    private fun cityFromDatabase(countryCode: String, cityName: String): MutableMap<String, Any> {
        return template.queryForMap(
            "SELECT * from organisations_schema.cities where country_code = '$countryCode' and name = '$cityName' LIMIT 1"
        )
    }

    companion object {
        @JvmStatic
        fun invalidOrgAddressRequestPayloads(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "missing city id",
                    """
                        {
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
                            "city_id": "b63d0116-8b10-447d-91f6-92d3b518940a",
                            "pin_code": "10405",
                            "street_name": "Metzer Strasse",
                            "plot_number": "45",
                            "floor": "3"
                        }
                    """.trimIndent()
                ),
                Arguments.of(
                    "non existing org id",
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
            )
        }

        @JvmStatic
        fun validOrgAddressRequestPayloads(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "full payload",
                    mutableMapOf<String, Any>(
                        "pin_code" to "10405",
                        "street_name" to "Metzer Strasse",
                        "plot_number" to "45",
                        "floor" to "3",
                        "apartment_number" to "13"
                    )
                ),
                Arguments.of(
                    "missing floor",
                    mutableMapOf<String, Any>(
                        "pin_code" to "10405",
                        "street_name" to "Metzer Strasse",
                        "plot_number" to "45",
                        "apartment_number" to "13"
                    )
                ),
                Arguments.of(
                    "missing apartment number",
                    mutableMapOf<String, Any>(
                        "pin_code" to "10405",
                        "street_name" to "Metzer Strasse",
                        "plot_number" to "45",
                        "floor" to "3",
                    )
                ),
                Arguments.of(
                    "int plot, floor and apartmentNumber",
                    mutableMapOf<String, Any>(
                        "pin_code" to "10405",
                        "street_name" to "Metzer Strasse",
                        "plot_number" to 45,
                        "floor" to 3,
                        "apartment_number" to 13
                    )
                ),
                Arguments.of(
                    "null floor and apartmentNumber",
                    mutableMapOf<String, Any?>(
                        "pin_code" to "10405",
                        "street_name" to "Metzer Strasse",
                        "plot_number" to "45",
                        "floor" to null,
                        "apartment_number" to null
                    )
                ),
            )
        }
    }

}
