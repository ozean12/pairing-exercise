package io.billie.address.persistence.model

import java.util.UUID
import javax.persistence.*

@Entity
@Table(schema = "organisations_schema", name = "countries")
data class CountryEntity(
    @Id
    @GeneratedValue
    val id: UUID,
    @Column(nullable = false, length = 100)
    val name: String,
    @Column(name="country_code", nullable = false, length = 2)
    val code: String
)
