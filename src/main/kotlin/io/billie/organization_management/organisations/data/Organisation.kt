package io.billie.organization_management.organisations.data

import io.billie.organization_management.countries.data.Country
import io.billie.organization_management.organisations.model.LegalEntityType
import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "organisations")
class Organisation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID?,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val dateFounded: LocalDate,
    @ManyToOne
    val country: Country,
    @Column(nullable = false, length = 20)
    val vatNumber: String,
    @Column(nullable = false, length = 20)
    val registrationNumber: String,
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val legalEntityType: LegalEntityType,
    @OneToOne(cascade = [CascadeType.ALL])
    val contactDetail: ContactDetail,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var address: Address? = null,
)
