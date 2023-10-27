package io.billie.organization_management.organisations.data

import io.billie.organization_management.countries.data.City
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "addresses")
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID?,
    @Column(length = 50)
    val street: String,
    @Column(length = 20)
    val number: String,
    @Column(length = 20)
    val postalCode: String,
    @ManyToOne
    val city: City
)
