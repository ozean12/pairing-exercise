package io.billie.functional.merchants

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.customers.data.CustomerRepository
import io.billie.functional.data.generateCustomerRequest
import io.billie.functional.data.generateMerchantRequest
import io.billie.functional.data.generateOrderRequest
import io.billie.invoicing.data.InvoiceRepository
import io.billie.invoicing.model.InvoiceStatus
import io.billie.merchants.data.MerchantRepository
import io.billie.merchants.dto.ShipmentNotification
import io.billie.orders.data.OrderRepository
import io.billie.organisations.model.Entity
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.util.UUID

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
class MerchantShipmentTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var template: JdbcTemplate

    @Autowired
    private lateinit var merchantRepository: MerchantRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var invoiceRepository: InvoiceRepository

    @Autowired
    private lateinit var jacksonTester: JacksonTester<ShipmentNotification>

    private final val noOfInstallments = 2L

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

    fun generateOrder(customerId: UUID, amount: BigDecimal): UUID {
        val orderRequest = generateOrderRequest(customerId, amount)
        return orderRepository.createOrder(orderRequest)
    }

    @Test
    fun `notifying of shipment should generate invoices for that customer with installments`() {
        val merchantId = generateMerchant()
        val customerId = generateCustomer()
        val orderId = generateOrder(customerId, BigDecimal.valueOf(100.00))

        val shipmentNotification = ShipmentNotification(
                shipmentUid = UUID.randomUUID(),
                orderUId = orderId,
                customerUId = customerId
        )

        val shipmentNotificationResult = mockMvc
                .perform(postShipmentNotification(merchantId, shipmentNotification))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()

        val invoiceEntity = mapper.readValue(shipmentNotificationResult.response.contentAsString, Entity::class.java)

        Assertions.assertThat(invoiceEntity.id).isNotNull
        Assertions.assertThat(invoiceEntity.id.toString().length).isEqualTo(36)

        val invoiceCheck = invoiceRepository.findCustomerInvoiceByUid(invoiceEntity.id)
        Assertions.assertThat(invoiceCheck.isPresent).isTrue

        val invoice = invoiceCheck.get()
        Assertions.assertThat(invoice.id).isEqualTo(invoiceEntity.id)
        Assertions.assertThat(invoice.customerUid).isEqualTo(customerId)
        Assertions.assertThat(invoice.orderUid).isEqualTo(orderId)
        Assertions.assertThat(invoice.status).isEqualTo(InvoiceStatus.CREATED)
        Assertions.assertThat(invoice.installments.size).isEqualTo(noOfInstallments)

        val installmentAmount = invoice.orderAmount.divide(BigDecimal.valueOf(noOfInstallments))

        val installment1 = invoice.installments[0]
        Assertions.assertThat(installment1.customerUid).isEqualTo(customerId)
        Assertions.assertThat(installment1.orderUid).isEqualTo(orderId)
        Assertions.assertThat(installment1.status).isEqualTo(InvoiceStatus.CREATED)
        Assertions.assertThat(installment1.installmentAmount).isEqualTo(installmentAmount)

        val installment2 = invoice.installments[1]
        Assertions.assertThat(installment2.customerUid).isEqualTo(customerId)
        Assertions.assertThat(installment2.orderUid).isEqualTo(orderId)
        Assertions.assertThat(installment2.status).isEqualTo(InvoiceStatus.CREATED)
        Assertions.assertThat(installment2.installmentAmount).isEqualTo(installmentAmount)
    }

    private fun postShipmentNotification(merchantUid: UUID, shipmentNotification: ShipmentNotification) =
        post("/merchants/{merchantUid}/notifyShipment", merchantUid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(shipmentNotification).json)


    @Test
    fun `notifying of shipment should return 404 when merchant id doesn't exists `() {
        val merchantId = UUID.randomUUID()
        val customerId = generateCustomer()
        val orderId = generateOrder(customerId, BigDecimal.valueOf(100.00))

        val shipmentNotification = ShipmentNotification(
                shipmentUid = UUID.randomUUID(),
                orderUId = orderId,
                customerUId = customerId
        )

        mockMvc
                .perform(postShipmentNotification(merchantId, shipmentNotification))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `notifying of shipment should return 400 when customer id doesn't exists `() {
        val merchantId = generateMerchant()
        val customerId = UUID.randomUUID()
        val orderId = generateOrder(customerId, BigDecimal.valueOf(120.00))

        val shipmentNotification = ShipmentNotification(
                shipmentUid = UUID.randomUUID(),
                orderUId = orderId,
                customerUId = customerId
        )

        mockMvc
                .perform(postShipmentNotification(merchantId, shipmentNotification))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `notifying of shipment should return 400 when order id doesn't exists `() {
        val merchantId = generateMerchant()
        val customerId = generateCustomer()
        val orderId = UUID.randomUUID()

        val shipmentNotification = ShipmentNotification(
                shipmentUid = UUID.randomUUID(),
                orderUId = orderId,
                customerUId = customerId
        )

        mockMvc
                .perform(postShipmentNotification(merchantId, shipmentNotification))
                .andExpect(status().isBadRequest)
    }
}