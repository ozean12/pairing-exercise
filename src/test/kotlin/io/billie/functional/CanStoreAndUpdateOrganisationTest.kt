package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.billie.functional.data.Fixtures.orgAfterUpdateJson
import io.billie.functional.data.Fixtures.orgBeforeUpdateJson
import io.billie.organisations.viewmodel.Entity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import org.assertj.core.api.Assertions.*

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class CanStoreAndUpdateOrganisationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

    @Test
    fun `Can store and update org`() {
        val result = mockMvc.perform(
            post("/organisations").contentType(APPLICATION_JSON).content(orgBeforeUpdateJson())
        )
        .andExpect(status().isOk)
        .andReturn()

        val response = mapper.readValue<Entity>(result.response.contentAsString)

        val org: Map<String, Any> = queryEntityFromDatabase(response.id)
        assertThat(org["address"]).isEqualTo("123 Story Lane")

        val updateResult = mockMvc.perform(
            put("/organisations/${response.id}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(orgAfterUpdateJson())
        )
            .andExpect(status().isOk)
            .andReturn()

        val updateResponse = mapper.readValue<Entity>(updateResult.response.contentAsString)

        assertThat(response.id).isEqualTo(updateResponse.id)

        val updatedOrg: Map<String, Any> = queryEntityFromDatabase(updateResponse.id)

        assertThat(updatedOrg["address"]).isEqualTo("456 Horse Road")
        assertThat(updatedOrg["vat_number"]).isEqualTo("555555555")
        assertThat(updatedOrg["registration_number"]).isEqualTo("555555555")
        assertThat(updatedOrg["name"]).isEqualTo("My Organisation")
    }

    private fun queryEntityFromDatabase(id: UUID): MutableMap<String, Any> =
        template.queryForMap("select * from organisations_schema.organisations where id = ?", id)

}
