package io.billie.functional

import io.billie.functional.matcher.IsUUID.isUuid
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.jupiter.api.Disabled
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

    @Disabled
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
            .andExpect(jsonPath("$.[0].name").value("Harare"))
            .andExpect(jsonPath("$.[0].id").value(isUuid()))
            .andExpect(jsonPath("$.[25].name").value("Mazoe"))
            .andExpect(jsonPath("$.[25].id").value(isUuid()))
    }

    @Test
    fun canViewBECities() {
        mockMvc.perform(
            get("/countries/be/cities")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].name").value("Brussels"))
            .andExpect(jsonPath("$.[0].id").value(isUuid()))
            .andExpect(jsonPath("$.size()").value(468))
            .andExpect(jsonPath("$.[467].name").value("Alveringem"))
            .andExpect(jsonPath("$.[467].id").value(isUuid()))
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
            .andExpect(jsonPath("$.[239].name").value("Zimbabwe"))
            .andExpect(jsonPath("$.[239].id").value(isUuid()))
    }

}
