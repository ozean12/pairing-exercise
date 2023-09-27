package io.billie.organization_management

import io.billie.organization_management.matcher.IsUUID.isUuid
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
class CanReadLocationsTest {

    companion object {

        @Container
        @ServiceConnection
        val postgreSQLContainer = PostgreSQLContainer("postgres:latest")
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 404 when country with given code is not available`() {
        mockMvc.perform(
            get("/countries/xx/cities")
                .contentType(APPLICATION_JSON),
        ).andExpect(status().isNotFound)
    }

    @Test
    fun `should return 200 when Kerman city of Iran(ir) is requested`() {
        mockMvc.perform(
            get("/countries/IR/cities/Kerman")
                .contentType(APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Kerman"))
            .andExpect(jsonPath("$.id").value(isUuid()))
            .andExpect(jsonPath("$.country_code").value("IR"))
    }

    @Test
    fun `should return 200 when cities of Zimbabwe(zw) are requested`() {
        mockMvc.perform(
            get("/countries/zw/cities")
                .contentType(APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].name").value("Harare"))
            .andExpect(jsonPath("$.[0].id").value(isUuid()))
            .andExpect(jsonPath("$.[0].country_code").value("ZW"))
            .andExpect(jsonPath("$.[25].name").value("Mazoe"))
            .andExpect(jsonPath("$.[25].id").value(isUuid()))
            .andExpect(jsonPath("$.[25].country_code").value("ZW"))
    }

    @Test
    fun `should return 200 when cities of Belgium(be) are requested`() {
        mockMvc.perform(
            get("/countries/be/cities")
                .contentType(APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].name").value("Brussels"))
            .andExpect(jsonPath("$.[0].id").value(isUuid()))
            .andExpect(jsonPath("$.[0].country_code").value("BE"))
            .andExpect(jsonPath("$.size()").value(468))
            .andExpect(jsonPath("$.[467].name").value("Alveringem"))
            .andExpect(jsonPath("$.[467].id").value(isUuid()))
            .andExpect(jsonPath("$.[467].country_code").value("BE"))
    }

    @Test
    fun `should return 200 when all countries are requested`() {
        mockMvc.perform(
            get("/countries")
                .contentType(APPLICATION_JSON),
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
