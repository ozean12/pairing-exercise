package io.billie.organization_management.organisations.data

class CountryNotFoundException(countryCode: String) : RuntimeException("Country with code $countryCode not found")
