package io.billie.address.resource.rest

import io.billie.address.model.City
import io.billie.address.model.Country
import io.billie.address.service.CityService
import io.billie.address.service.CountryService
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("countries")
class CountriesResource(val countryService: CountryService, val cityService: CityService) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "All countries",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Country::class)))
                    ))]
            )]
    )
    @GetMapping
    fun index(): List<Country> = countryService.getAll()

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found cities for country",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = City::class)))
                    ))]
            )]
    )
    @GetMapping("/{countryCode}/cities")
    fun cities(@PathVariable("countryCode") countryCode: String): List<City> {
        return cityService.getCities(countryCode.uppercase())
    }

}
