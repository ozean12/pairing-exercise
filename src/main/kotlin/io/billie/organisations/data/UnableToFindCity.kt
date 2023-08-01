package io.billie.organisations.data

class UnableToFindCity(cityId: String) : RuntimeException("City not found for id $cityId")
