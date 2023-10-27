package io.billie.countries.service

import io.billie.countries.data.CityRepository
import io.billie.countries.data.CountryRepository
import io.billie.countries.exception.CountryNotFoundException
import io.billie.countries.model.CityResponse
import io.billie.countries.model.CountryResponse
import org.springframework.stereotype.Service

@Service
class CountryService(val dbCountry: CountryRepository, val dbCity: CityRepository, ) {

    fun findCountries(): List<CountryResponse> {
        return dbCountry.findCountries()
    }
    fun findCities(countryCode: String): List<CityResponse> = dbCity.findByCountryCode(countryCode)

    fun findCountryByCode (countryCode: String): CountryResponse {
        return dbCountry.findCountryByCode(countryCode).orElseThrow { CountryNotFoundException("country with code: $countryCode was not found!") }
    }
}
