package com.leopoldhsing.digitalhippo.product.service.impl

import com.leopoldhsing.digitalhippo.common.exception.ResourceNotFoundException
import com.leopoldhsing.digitalhippo.common.utils.RequestUtil
import com.leopoldhsing.digitalhippo.model.dto.ProductSearchingConditionDto
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.product.repository.ProductRepository
import com.leopoldhsing.digitalhippo.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl @Autowired constructor(
    private val productRepository: ProductRepository
) : ProductService {

    override fun conditionalSearchProducts(condition: ProductSearchingConditionDto): List<Product> {
        TODO("Not yet implemented")
    }

    override fun getProduct(productId: Long): Product {
        val product = productRepository.findByIdOrNull(productId)
        if (product == null) {
            throw ResourceNotFoundException("product", "id", productId.toString())
        }
        return product
    }

    override fun createProduct(product: Product): Product {
        return Product()
    }

    override fun updateProduct(product: Product): Product {
        // 1. get user
        val userId = RequestUtil.getUid()
        return Product()
    }

    override fun deleteProduct(productUrl: String) {

    }
}