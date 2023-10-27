package io.billie.organization_management.countries.service

import io.billie.organization_management.countries.data.CityRepository
import io.billie.organization_management.countries.data.CountryRepository
import io.billie.organization_management.countries.model.CityResponse
import io.billie.organization_management.countries.model.CountryResponse
import io.billie.organization_management.organisations.data.CityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CountryService(
    private val countryRepository: CountryRepository,
    private val cityRepository: CityRepository,
) {

    @Transactional(readOnly = true)
    fun findCountries(): List<CountryResponse> {
        return countryRepository.findAll().map {
            CountryResponse(
                id = it.id!!,
                name = it.name,
                countryCode = it.code,
            )
        }
    }

    @Transactional(readOnly = true)
    fun findCities(countryCode: String): List<CityResponse> {
        return cityRepository.findAllByCountryCodeIgnoreCase(countryCode).map {
            CityResponse(
                id = it.id!!,
                name = it.name,
                countryCode = it.countryCode,
            )
        }
    }

    fun findCity(countryCode: String, cityName: String): CityResponse {
        val city = cityRepository.findByCountryCodeIgnoreCaseAndNameIgnoreCase(countryCode, cityName) ?: throw CityNotFoundException(countryCode, cityName)

        return CityResponse(
            id = city.id!!,
            name = city.name,
            countryCode = city.countryCode,
        )
    }
}
