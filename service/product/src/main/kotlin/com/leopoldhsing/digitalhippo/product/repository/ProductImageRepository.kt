package com.leopoldhsing.digitalhippo.product.repository

import com.leopoldhsing.digitalhippo.model.entity.ProductImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductImageRepository: JpaRepository<ProductImage, Long> {

    fun deleteProductImageByIdIn(productImageIdList: List<Long>)
}