package io.billie.organisations.service

import io.billie.countries.data.CityRepository
import io.billie.countries.data.CountryRepository
import io.billie.functional.data.Fixtures.validAddressDetailsRequest
import io.billie.functional.data.Fixtures.validOrganisationRequest
import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.data.UnableToFindCity
import io.billie.organisations.data.UnableToFindCountry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.util.*

class OrganisationServiceTest {

    private val organisationRepository: OrganisationRepository = mockk()
    private val countryRepository: CountryRepository = mockk()
    private val cityRepository: CityRepository = mockk()

    private val sut: OrganisationService =
        OrganisationService(organisationRepository, countryRepository, cityRepository)

    @Test
    fun canCreateOrganisationWithoutAddressDetails() {
        val expected = UUID.randomUUID()
        every { organisationRepository.create(any()) } returns expected
        every { countryRepository.isCountryCodeValid(any()) } returns true

        val result = sut.createOrganisation(validOrganisationRequest())

        assert(result == expected)
        verify(exactly = 1) { countryRepository.isCountryCodeValid(any()) }
        verify(exactly = 0) { cityRepository.isCityIdValid(any()) }
    }

    @Test
    fun canCreateOrganisationWithAddressDetails() {
        val expected = UUID.randomUUID()
        every { organisationRepository.create(any()) } returns expected
        every { countryRepository.isCountryCodeValid(any()) } returns true
        every { cityRepository.isCityIdValid(any()) } returns true

        val result = sut.createOrganisation(validOrganisationRequest(addressDetails = validAddressDetailsRequest()))

        assert(result == expected)
        verify(exactly = 2) { countryRepository.isCountryCodeValid(any()) }
        verify(exactly = 1) { cityRepository.isCityIdValid(any()) }
    }

    @Test
    fun cantCreateOrganisationWithInvalidCountryCode() {
        every { organisationRepository.create(any()) } returns UUID.randomUUID()
        every { countryRepository.isCountryCodeValid(eq("INV")) } returns false

        assertThrows<UnableToFindCountry> { sut.createOrganisation(validOrganisationRequest(countryCode = "INV")) }
    }
    @Test
    fun cantCreateOrganisationWithInvalidCountryCodeInAddressDetails() {
        every { organisationRepository.create(any()) } returns UUID.randomUUID()
        every { countryRepository.isCountryCodeValid(eq("GB")) } returns true
        every { countryRepository.isCountryCodeValid(eq("INV")) } returns false
        every { cityRepository.isCityIdValid(any()) } returns true

        assertThrows<UnableToFindCountry> {  sut.createOrganisation(validOrganisationRequest(addressDetails = validAddressDetailsRequest(countryCode = "INV"))) }
    }
    @Test
    fun cantCreateOrganisationWithDifferentCountryCodeInAddressDetails() {
        every { organisationRepository.create(any()) } returns UUID.randomUUID()
        every { countryRepository.isCountryCodeValid(eq("GB")) } returns true
        every { countryRepository.isCountryCodeValid(eq("US")) } returns true
        every { cityRepository.isCityIdValid(any()) } returns true

        assertThrows<IllegalArgumentException> {  sut.createOrganisation(validOrganisationRequest(addressDetails = validAddressDetailsRequest(countryCode = "US"))) }
    }
    @Test
    fun cantCreateOrganisationWithInvalidCityIdInAddressDetails() {
        every { organisationRepository.create(any()) } returns UUID.randomUUID()
        every { countryRepository.isCountryCodeValid(any()) } returns true
        every { cityRepository.isCityIdValid(any()) } returns false

        assertThrows<UnableToFindCity> {  sut.createOrganisation(validOrganisationRequest(addressDetails = validAddressDetailsRequest())) }
    }
}