package com.leopoldhsing.digitalhippo.product.service.impl

import com.leopoldhsing.digitalhippo.model.dto.ProductSearchingConditionDto
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.product.service.ProductService
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl : ProductService {

    override fun conditionalSearchProducts(condition: ProductSearchingConditionDto): List<Product> {
        TODO("Not yet implemented")
    }

    override fun getProduct(productId: Long): Product {
        TODO("Not yet implemented")
    }

    override fun createProduct(product: Product): Product {
        TODO("Not yet implemented")
    }

    override fun updateProduct(product: Product): Product {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(productId: Long) {
        TODO("Not yet implemented")
    }
}