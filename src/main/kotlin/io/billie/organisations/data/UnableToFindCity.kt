package io.billie.organisations.data

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UnableToFindCity(cityId: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to find city with id: $cityId")