package io.billie.organization_management.countries.data

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "cities")
class City(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID?,
    @Column(nullable = false, length = 100)
    val name: String,
    @Column(nullable = false, length = 2)
    val countryCode: String,
)
