package io.billie.organisations.data

class UnableToFindCountry(
    val countryCode: String
) : RuntimeException(
    "Country code [$countryCode] not found"
)
