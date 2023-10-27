package io.billie.functional

import io.billie.functional.matcher.IsUUID.isUuid
import org.hamcrest.Description
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class CanReadLocationsTest {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun notFoundForUnknownCountry() {
        mockMvc.perform(
            get("/countries/xx/cities")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isNotFound)
    }

    @Test
    fun canViewZWCities() {
        mockMvc.perform(
            get("/countries/zw/cities")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].name", hasItems("Mazoe", "Harare")))
            .andExpect(jsonPath("$[*].country_code", everyItem(equalTo("ZW"))))
            .andExpect(jsonPath("$[*].id", everyItem(isUuid())))
    }

    // TODO check the requirements, API doesnt support ordering, thus no ordering
    // can be guaranteed, therefore changing tests to check data exists
    @Test
    fun canViewBECities() {
        mockMvc.perform(
            get("/countries/be/cities")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].name", hasItems("Brussels", "Alveringem")))
            .andExpect(jsonPath("$[*].country_code", everyItem(equalTo("BE"))))
            .andExpect(jsonPath("$[*].id", everyItem(isUuid())))
    }

    @Test
    fun canViewCountries() {
        mockMvc.perform(
            get("/countries")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].name").value("Andorra"))
            .andExpect(jsonPath("$.[0].id").value(isUuid()))
            .andExpect(jsonPath("$.[0].country_code").value("AD"))
            .andExpect(jsonPath("$.[239].name").value("Zimbabwe"))
            .andExpect(jsonPath("$.[239].id").value(isUuid()))
            .andExpect(jsonPath("$.[239].country_code").value("ZW"))
    }

}
