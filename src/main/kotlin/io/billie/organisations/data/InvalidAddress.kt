package io.billie.organisations.data

class InvalidAddress(city: String) : ValidationException("City $city not found")
