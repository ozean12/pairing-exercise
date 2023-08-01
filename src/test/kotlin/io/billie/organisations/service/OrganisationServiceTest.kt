package io.billie.organisations.service

import io.billie.countries.data.CityRepository
import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.data.UnableToFindCity
import io.billie.organisations.data.UnableToFindOrganisation
import io.billie.organisations.viewmodel.AddressRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class OrganisationServiceTest {

    @Mock
    private lateinit var organisationRepository: OrganisationRepository

    @Mock
    private lateinit var cityRepository: CityRepository

    @InjectMocks
    private lateinit var organisationService: OrganisationService

    @Test
    fun `validateCityExists throws UnableToFindCity exception when city does not exist`() {
        val organisationId = UUID.randomUUID().toString()
        val cityId = UUID.randomUUID().toString()
        Mockito.`when`(organisationRepository.existsById(UUID.fromString(organisationId))).thenReturn(true)
        Mockito.`when`(cityRepository.existsById(UUID.fromString(cityId))).thenReturn(false)

        assertThrows(UnableToFindCity::class.java) {
            organisationService.addAddressToOrganisation(
                organisationId,
                AddressRequest(cityId = cityId, "", "", "")
            )
        }
    }

    @Test
    fun `validateOrganisationExists throws UnableToFindOrganisation exception when organisation does not exist`() {
        val organisationId = UUID.randomUUID().toString()
        val cityId = UUID.randomUUID().toString()
        Mockito.`when`(organisationRepository.existsById(UUID.fromString(organisationId))).thenReturn(false)

        assertThrows(UnableToFindOrganisation::class.java) {
            organisationService.addAddressToOrganisation(
                organisationId,
                AddressRequest(cityId = cityId, "", "", ""))
        }
    }
}
