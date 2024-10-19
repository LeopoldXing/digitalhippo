package com.leopoldhsing.digitalhippo.product.service

import com.leopoldhsing.digitalhippo.model.entity.Product

interface ProductCacheService {

    fun getProductFromCache(productId: String): Product?

    fun saveProductToCache(productId: String, product: Product?)

    fun hasProduct(productId: Long): Boolean

    fun addProductToBitmap(productId: Long)

    fun removeProductFromBitmap(productId: Long)
}