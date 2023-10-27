package io.billie.products.service

import io.billie.products.data.ProductRepository
import io.billie.products.dto.ProductRequest
import io.billie.products.exception.ProductNotFoundException
import io.billie.products.model.Product
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductServiceImpl (private val productRepository: ProductRepository) : ProductService {
    override fun findByProductId(productUid: UUID): Product {
        return productRepository.findProductById(productUid)
                .orElseThrow { ProductNotFoundException("product with uuid: $productUid was not found!") }
    }

    override fun findProducts(): List<Product> {
        return productRepository.findProducts()
    }

    override fun createProduct(productRequest: ProductRequest): UUID {
        return productRepository.createProduct(productRequest)
    }
}