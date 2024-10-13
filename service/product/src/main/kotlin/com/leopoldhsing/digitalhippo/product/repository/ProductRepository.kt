package com.leopoldhsing.digitalhippo.product.repository

import com.leopoldhsing.digitalhippo.model.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

    fun findProductByProductFileUrlEndsWith(uri: String): Product?

    fun findProductByPayloadId(id: String): Product?

    fun findProductsByIdIn(ids: List<Long>): List<Product>

}