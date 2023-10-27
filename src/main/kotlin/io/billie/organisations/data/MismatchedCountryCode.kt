package io.billie.organisations.data

class MismatchedCountryCode(countryCode: String, addressCountryCode: String) : ValidationException(
    "Mismatched country codes: organisation has $countryCode but address has $addressCountryCode")
