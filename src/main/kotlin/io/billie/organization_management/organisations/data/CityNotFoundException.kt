package io.billie.organization_management.organisations.data

import java.util.*

class CityNotFoundException(message: String) : RuntimeException(message) {
    constructor(cityId: UUID) : this("City with id $cityId not found")
    constructor(countryCode: String, cityName: String) : this("City with name $cityName for country $countryCode not found")
}
