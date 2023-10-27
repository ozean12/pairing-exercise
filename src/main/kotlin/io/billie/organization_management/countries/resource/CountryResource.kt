package io.billie.organization_management.countries.resource

import io.billie.organization_management.countries.model.CityResponse
import io.billie.organization_management.countries.model.CountryResponse
import io.billie.organization_management.countries.service.CountryService
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("countries")
class CountryResource(private val countryService: CountryService) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "All countries",
                content = [
                    (
                        Content(
                            mediaType = "application/json",
                            array = (ArraySchema(schema = Schema(implementation = CountryResponse::class))),
                        )
                        ),
                ],
            ),
        ],
    )
    @GetMapping
    fun countries(): List<CountryResponse> = countryService.findCountries()

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found cities for country",
                content = [
                    (
                        Content(
                            mediaType = "application/json",
                            array = (ArraySchema(schema = Schema(implementation = CityResponse::class))),
                        )
                        ),
                ],
            ),
            ApiResponse(responseCode = "404", description = "No cities found for country code", content = [Content()]),
        ],
    )
    @GetMapping("/{countryCode}/cities")
    fun cities(@PathVariable("countryCode") countryCode: String): List<CityResponse> {
        val cities = countryService.findCities(countryCode)
        if (cities.isEmpty()) {
            throw ResponseStatusException(
                NOT_FOUND,
                "No cities found for $countryCode",
            )
        }
        return cities
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found city for country",
                content = [
                    (
                        Content(
                            mediaType = "application/json",
                            array = (ArraySchema(schema = Schema(implementation = CityResponse::class))),
                        )
                        ),
                ],
            ),
            ApiResponse(responseCode = "404", description = "No city found for city name and country code", content = [Content()]),
        ],
    )
    @GetMapping("/{countryCode}/cities/{cityName}")
    fun city(
        @PathVariable("countryCode") countryCode: String,
        @PathVariable("cityName") cityName: String,
    ): CityResponse {
        return countryService.findCity(countryCode, cityName)
    }
}
