package io.billie.functional.merchants

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.generateMerchantRequest
import io.billie.merchants.data.MerchantRepository
import io.billie.merchants.dto.MerchantRequest
import io.billie.merchants.dto.MerchantResponse
import io.billie.organisations.model.Entity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MerchantResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

    @Autowired
    private lateinit var merchantRepository: MerchantRepository

    @Autowired
    private lateinit var jacksonTester: JacksonTester<MerchantRequest>

    @AfterEach
    fun cleanTablesBeforeTest() {
        template.execute("DELETE FROM organisations_schema.merchants")
    }

    private fun createMerchants(name:  String): UUID{
        return merchantRepository.createMerchant(generateMerchantRequest(name))
    }

    @Test
    fun `should return all merchants`() {
        val merchantId1 = createMerchants("demo1")
        val merchantId2 = createMerchants("demo2")

        val findMerchant = mockMvc
                .perform(get("/merchants"))
                .andExpect(status().isOk)
                .andReturn()

        val merchantResponses = mapper.readValue(findMerchant.response.contentAsString, Array<MerchantResponse>::class.java)
        Assertions.assertEquals(merchantResponses.size, 2)

        // verify results
        val firstMerchant = merchantResponses[0]
        Assertions.assertEquals("demo1",firstMerchant.name)
        Assertions.assertEquals(merchantId1, firstMerchant.id)

        val secondMerchant = merchantResponses[1]
        Assertions.assertEquals("demo2",secondMerchant.name)
        Assertions.assertEquals(merchantId2, secondMerchant.id)
    }

    @Test
    fun `should return empty when no merchants exists`() {
        val findMerchant = mockMvc
                .perform(get("/merchants"))
                .andExpect(status().isOk)
                .andReturn()

        val merchantResponses = mapper.readValue(findMerchant.response.contentAsString, Array<MerchantResponse>::class.java)
        Assertions.assertEquals(merchantResponses.size, 0)
    }

    @Test
    fun `should find the merchant with uuid when id exists`() {
        val merchantId1 = createMerchants("demo1")

        val findMerchant = mockMvc
                .perform(get("/merchants/{merchantUid}", merchantId1))
                .andExpect(status().isOk)
                .andReturn()

        val merchantResponse = mapper.readValue(findMerchant.response.contentAsString, MerchantResponse::class.java)

        // validate the response
        Assertions.assertEquals("demo1",merchantResponse.name)
        Assertions.assertEquals(merchantId1, merchantResponse.id)
    }

    @Test
    fun `find merchant by Uid should return 404 when uid does not exists`() {
        val merchantId = UUID.randomUUID()
        mockMvc
            .perform(get("/merchants/{merchantUid}", merchantId))
            .andExpect(status().isNotFound)
            .andReturn()
    }

    @Test
    fun `create merchant should return uuid of new merchant`(){
        val merchantRequest = generateMerchantRequest("demo")

        val createMerchantResult = mockMvc
                .perform(postMerchantRequest(merchantRequest))
                .andExpect(status().isCreated)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()

        val merchantEntity = mapper.readValue(createMerchantResult.response.contentAsString, Entity::class.java)

        assertThat (merchantEntity.id).isNotNull
        assertThat(merchantEntity.id.toString().length).isEqualTo(36)
    }

    private fun postMerchantRequest(request: MerchantRequest) =
            post("/merchants")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jacksonTester.write(request).json)
}