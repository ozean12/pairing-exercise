package io.billie.products.service

import io.billie.products.dto.ProductRequest
import io.billie.products.model.Product
import java.util.*

interface ProductService {
    fun createProduct (productRequest: ProductRequest): UUID
    fun findProducts(): List<Product>
    fun findByProductId (productUid: UUID): Product
}