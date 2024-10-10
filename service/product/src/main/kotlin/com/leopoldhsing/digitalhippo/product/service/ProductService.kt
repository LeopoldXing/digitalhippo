package com.leopoldhsing.digitalhippo.product.service

import com.leopoldhsing.digitalhippo.model.dto.ProductSearchingConditionDto
import com.leopoldhsing.digitalhippo.model.entity.Product

interface ProductService {

    fun conditionalSearchProducts(condition: ProductSearchingConditionDto): List<Product>
    fun getProduct(productId: Long): Product
    fun createProduct(product: Product): Product
    fun updateProduct(product: Product): Product
    fun deleteProduct(productUrl: String)
}