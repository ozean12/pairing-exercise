package io.billie

import io.billie.organisations.data.UnableToFindCity
import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.data.UnableToFindOrganisation
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ExceptionHandlerTest {

    @Test
    fun `getHttpException returns BAD_REQUEST for UnableToFindCountry`() {
        val exception = UnableToFindCountry("DE")

        val result = getHttpException(exception)

        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals("Country not found by code DE", result.reason)
    }

    @Test
    fun `getHttpException returns BAD_REQUEST for UnableToFindOrganisation`() {
        val orgId = "123"
        val exception = UnableToFindOrganisation(orgId)

        val result = getHttpException(exception)

        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals("Organisation not found by id $orgId", result.reason)
    }

    @Test
    fun `getHttpException returns BAD_REQUEST for UnableToFindCity`() {
        val cityId = "123"
        val exception = UnableToFindCity(cityId)

        val result = getHttpException(exception)

        assertEquals(HttpStatus.BAD_REQUEST, result.status)
        assertEquals("City not found for id $cityId", result.reason)
    }

    @Test
    fun `getHttpException returns INTERNAL_SERVER_ERROR for any other exception`() {
        val exception = RuntimeException("Runtime exception")

        val result = getHttpException(exception)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.status)
    }
}
