package io.billie.merchants.dto

import java.util.*
import javax.validation.constraints.NotBlank

data class ShipmentNotification(
        @field:NotBlank val shipmentUid: UUID,
        @field:NotBlank val orderUId: UUID,
        @field:NotBlank val customerUId: UUID,
)
