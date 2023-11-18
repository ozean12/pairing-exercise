package io.billie.organisations.persistence.model

import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "organisations_schema", name = "contact_details")
data class ContactDetailsEntity(
    @Id
    @GeneratedValue
    val id: UUID?,
    @Column(name = "phone_number", length = 20)
    val phoneNumber: String?,
    @Column(name = "fax", length = 20)
    val fax: String?,
    @Column(name = "email", length = 256)
    val email: String?,
    @OneToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    var organizationEntity: OrganizationEntity?
)
