package io.billie

import io.billie.organisations.data.UnableToFindCity
import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.data.UnableToFindOrganisation
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

fun getHttpException(e: Exception): ResponseStatusException {
    return when (e) {
        is UnableToFindCountry -> ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        is UnableToFindOrganisation -> ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        is UnableToFindCity -> ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        else -> ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
    }
}