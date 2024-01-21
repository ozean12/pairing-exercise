package io.billie.functional.data

import io.billie.organisations.orders.domain.ShipmentAmount

object ShipmentFixtures {

    fun shipmentRequestJson(shipmentAmount: ShipmentAmount): String {
        return "{\n" +
                "  \"amount\": ${shipmentAmount.amount}\n" +
                "}"
    }

    fun shipmentRequestJsonMissingAmount(): String {
        return "{\n" +
                "}"
    }

    fun shipmentRequestJsonInvalidAmount(): String {
        return "{\n" +
                "  \"amount\": -1\n" +
                "}"
    }
}