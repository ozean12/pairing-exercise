package io.billie.functional.invoicing

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.customers.data.CustomerRepository
import io.billie.functional.data.*
import io.billie.invoicing.data.InvoiceRepository
import io.billie.invoicing.dto.InvoiceResponse
import io.billie.invoicing.model.InvoiceStatus
import io.billie.invoicing.resource.InvoiceResource
import io.billie.invoicing.service.InvoiceService
import io.billie.merchants.data.MerchantRepository
import io.billie.orders.data.OrderRepository
import io.billie.products.data.ProductRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.util.*

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class InvoiceResourceTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Autowired
    private lateinit var merchantRepository: MerchantRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var invoiceRepository: InvoiceRepository

    @Autowired
    private lateinit var template: JdbcTemplate

    @Autowired
    private lateinit var jacksonTester: JacksonTester<InvoiceResource>

    @AfterEach
    fun cleanTablesBeforeTest() {
        template.execute("DELETE FROM organisations_schema.merchants")
        template.execute("DELETE FROM organisations_schema.customer_orders")
        template.execute("DELETE FROM organisations_schema.customers")
        template.execute("DELETE FROM organisations_schema.products")
        template.execute("DELETE FROM organisations_schema.invoices")
    }

    fun generateMerchant(): UUID {
        return merchantRepository.createMerchant(generateMerchantRequest("demo"))
    }

    fun generateCustomer(): UUID {
        return customerRepository.createCustomer(generateCustomerRequest())
    }

    fun generateOrder(customerId: UUID): UUID {
        val orderRequest = generateOrderRequest(customerId, BigDecimal.valueOf(120.00))
        return orderRepository.createOrder(orderRequest)
    }

    fun generateInvoice(customerId: UUID, orderId: UUID, merchantId: UUID): UUID{
        return invoiceRepository.createCustomerInvoices(createInvoiceRequest(orderId, customerId, merchantId))
    }

    @Test
    fun `findAll should return all invoices`() {
        val merchantId = generateMerchant()
        val customerId = generateCustomer()
        val orderId = generateOrder(customerId)
        val invoiceUid = generateInvoice(customerId, orderId, merchantId)

        val findAllInvoices = mockMvc
                .perform(get("/invoices"))
                .andExpect(status().isOk)
                .andReturn()

        val invoiceResponses = mapper.readValue(findAllInvoices.response.contentAsString, Array<InvoiceResponse>::class.java)
        Assertions.assertEquals(1,invoiceResponses.size)

        // verifying the results
        val firstInvoice = invoiceResponses[0]
        Assertions.assertEquals(invoiceUid,firstInvoice.id)
        Assertions.assertEquals(merchantId, firstInvoice.merchantUid)
        Assertions.assertEquals(orderId, firstInvoice.orderUid)
        Assertions.assertEquals(InvoiceStatus.CREATED, firstInvoice.status)
        Assertions.assertEquals(2, firstInvoice.installments.size)

    }

    @Test
    fun `should return empty when no invoices exists`() {
        val findMerchant = mockMvc
                .perform(get("/invoices"))
                .andExpect(status().isOk)
                .andReturn()

        val invoiceResponses = mapper.readValue(findMerchant.response.contentAsString, Array<InvoiceResponse>::class.java)
        Assertions.assertEquals(0,invoiceResponses.size)
    }
}