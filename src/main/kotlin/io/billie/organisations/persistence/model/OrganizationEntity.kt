package io.billie.organisations.persistence.model

import io.billie.organisations.model.LegalEntityType
import org.mapstruct.EnumMapping
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "organisations_schema", name = "organisations")
data class OrganizationEntity (
    @Id
    @GeneratedValue
    val id: UUID,
    @Column(nullable = false)
    val name: String,
    @Column(name = "date_founded", nullable = false)
    val foundedDate: LocalDate,
    @Column(name = "country_code", nullable = false, length = 2)
    val countryCode: String,
    @Column(name = "VAT_number", length = 20)
    val vatNumber: String,
    @Column(name = "registration_number", length = 20)
    val registrationNumber: String,
    @Column(name = "legal_entity_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val type: LegalEntityType,
    @Column(name = "contact_details_id", nullable = false, length = 36)
    val contactDetailsId: String
)
