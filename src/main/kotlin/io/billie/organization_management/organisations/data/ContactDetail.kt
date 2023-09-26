package io.billie.organization_management.organisations.data

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "contact_details")
class ContactDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID?,
    @Column(length = 20)
    val phoneNumber: String?,
    @Column(length = 20)
    val fax: String?,
    @Column(length = 256)
    val email: String?,
)
