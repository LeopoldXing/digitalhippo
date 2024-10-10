package com.leopoldhsing.digitalhippo.product.mapper

import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.model.vo.ProductVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class ProductMapper @Autowired constructor(private val productImageMapper: ProductImageMapper) {

    open fun mapToProduct(productVo: ProductVo, approvedForSale: String = "pending"): Product {
        val product = Product()
        product.name = productVo.filename
        product.description = productVo.description
        product.price = productVo.price
        product.productFileUrl = productVo.productFileUrl
        product.category = productVo.category
        product.approvedForSale = approvedForSale
        val productImageList =
            productVo.productImages?.map { productImageVo -> productImageMapper.mapTopProductImage(productImageVo, product) }
        product.productImages = productImageList

        return product
    }
}