package io.billie.unit.shipments.data

import io.billie.shared.PostgresqlSharedContainer
import io.billie.shipments.data.ShipmentRepository
import io.billie.shipments.model.Shipment
import io.billie.util.queryEntityFromDatabase
import io.billie.util.shipmentFromDatabase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*


@SpringBootTest()
class ShipmentRepositoryTest : PostgresqlSharedContainer() {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var shipmentRepository: ShipmentRepository

    @Test
    fun createShipment_success() {

        val orderId = UUID.randomUUID()
        val shipment = Shipment(
            orderId = orderId,
            shipmentAmount = BigDecimal("100.00"),
            shippedAt = LocalDateTime.now()
        )

        val shipmentId = shipmentRepository.createShipment(shipment)

        assertNotNull(shipmentId)

        val createdShipment = shipment.copy(id = shipmentId)
        val shipmentFromDatabase = jdbcTemplate.shipmentFromDatabase(shipmentId)
        assertNotNull(shipmentFromDatabase)
        assertEquals(shipmentFromDatabase["id"], createdShipment.id)
        assertEquals(shipmentFromDatabase["order_id"], createdShipment.orderId)
        assertEquals(shipmentFromDatabase["shipment_amount"], createdShipment.shipmentAmount)

    }

    @Test
    fun findTotalShippedAmountByOrder_success() {

        val orderId = UUID.randomUUID()

        val shipment1 = Shipment(
            orderId = orderId,
            shipmentAmount = BigDecimal("100.00"),
            shippedAt = LocalDateTime.now()
        )

        val shipment2 = Shipment(
            orderId = orderId,
            shipmentAmount = BigDecimal("200.00"),
            shippedAt = LocalDateTime.now()
        )

        shipmentRepository.createShipment(shipment1)
        shipmentRepository.createShipment(shipment2)

        val totalShippedAmount = shipmentRepository.findTotalShippedAmountByOrder(orderId)
        assertEquals(shipment1.shipmentAmount.plus(shipment2.shipmentAmount), totalShippedAmount)

    }


    @Test
    fun findByOrderId_success() {
        // Arrange
        val orderId = UUID.randomUUID()
        val shipment1 =
            Shipment(orderId = orderId, shipmentAmount = BigDecimal("100.00"), shippedAt = LocalDateTime.now())
        val shipment2 =
            Shipment(orderId = orderId, shipmentAmount = BigDecimal("200.00"), shippedAt = LocalDateTime.now())

        val shipmentId1 = shipmentRepository.createShipment(shipment1)
        val shipmentId2 = shipmentRepository.createShipment(shipment2)

        val shipments = shipmentRepository.findByOrderId(orderId)

        assertEquals(2, shipments.size)
        assertNotNull(shipments.find { it.id == shipmentId1 })
        assertNotNull(shipments.find { it.id == shipmentId2 })
    }

    @Test
    fun findByOrderId_emptyWhenNoShipments() {
        val orderId = UUID.randomUUID()

        val shipments = shipmentRepository.findByOrderId(orderId)

        assertTrue(shipments.isEmpty())
    }

    @Test
    fun findById_success() {

        val orderId = UUID.randomUUID()
        val shipment =
            Shipment(orderId = orderId, shipmentAmount = BigDecimal("100.00"), shippedAt = LocalDateTime.now())
        val shipmentId = shipmentRepository.createShipment(shipment)

        val foundShipment = shipmentRepository.findById(shipmentId)

        assertNotNull(foundShipment)
        assertEquals(shipmentId, foundShipment?.id)
        assertEquals(orderId, foundShipment?.orderId)
        assertEquals(shipment.shipmentAmount, foundShipment?.shipmentAmount)
    }

    @Test
    fun findById_nullWhenShipmentDoesNotExist() {
        val shipmentId = UUID.randomUUID()

        val foundShipment = shipmentRepository.findById(shipmentId)

        assertNull(foundShipment)
    }
}