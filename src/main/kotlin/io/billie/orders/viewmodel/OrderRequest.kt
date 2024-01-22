package io.billie.orders.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

// TODO implement this while build functionality of notifying billie about the order invoice
data class OrderRequest(@JsonProperty("organization_id") val organizationId: UUID)
