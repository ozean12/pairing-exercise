package io.billie.organisations.data

class UnableToFindCountry(countryCode: String) : RuntimeException("Country not found by code $countryCode")
