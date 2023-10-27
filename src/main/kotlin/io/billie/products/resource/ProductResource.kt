package io.billie.products.resource

import io.billie.organisations.model.Entity
import io.billie.products.dto.ProductRequest
import io.billie.products.dto.ProductResponse
import io.billie.products.mapper.toProductResponse
import io.billie.products.service.ProductService
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.streams.toList

@RestController
@RequestMapping ("/products")
class ProductResource (val productService: ProductService){

    @GetMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "returns all products ",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = ProductResponse::class)))
                            ))]
                )
            ]
    )
    fun findProducts (): ResponseEntity<List<ProductResponse>> {
        return ResponseEntity.ok(
                productService.findProducts()
                .stream().map { it.toProductResponse() }
                .toList())
    }

    @PostMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "201",
                        description = "Accepted the new product",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun createProduct (@RequestBody productRequest: ProductRequest): ResponseEntity<Entity>{
        val productUid = productService.createProduct(productRequest)

        return ResponseEntity.status(HttpStatus.CREATED).body(Entity(productUid))
    }
}