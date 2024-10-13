package com.leopoldhsing.digitalhippo.product.service

import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo

interface ProductService {

    fun conditionalSearchProducts(condition: ProductSearchingConditionVo): List<Product>
    fun getProduct(productId: Long): Product
    fun createProduct(product: Product): Product
    fun updateProduct(product: Product): Product
    fun deleteProduct(payloadId: String)
    fun getProducts(productIdList: List<Long>): List<Product>
}