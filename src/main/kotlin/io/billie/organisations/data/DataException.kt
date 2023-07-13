package io.billie.organisations.data

sealed class DataException : RuntimeException()

class UnableToFindCountry(private val countryCode: String) : DataException() {
    override val message: String
        get() = "Unable to find country $countryCode"
}

class UnableToFindCity(private val countryCode: String, private val city: String) : DataException() {
    override val message: String
        get() = "Unable to find city $city in country $countryCode"
}

