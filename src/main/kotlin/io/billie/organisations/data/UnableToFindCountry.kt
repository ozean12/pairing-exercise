package io.billie.organisations.data

class UnableToFindCountry(val countryCode: String) : ValidationException("Country code $countryCode not found")
