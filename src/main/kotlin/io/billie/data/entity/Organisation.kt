package io.billie.data.entity

import java.util.Date
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "organisations")
data class Organisation (
     @Id
    var id: UUID,
    var name: String,
    var dateFounded: Date,
    var countryCode: String,
    var vatNumber: String,
    var registrationNumber: String,
    var legalEntityType: String,
    var contactDetailsId: String

)
