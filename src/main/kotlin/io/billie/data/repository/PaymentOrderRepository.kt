package io.billie.data.repository

import io.billie.data.entity.PaymentOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PaymentOrderRepository: JpaRepository<PaymentOrder, UUID>
