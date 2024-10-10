package com.leopoldhsing.digitalhippo.product.mapper

import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.model.entity.ProductImage
import com.leopoldhsing.digitalhippo.model.enumeration.ProductFileType
import com.leopoldhsing.digitalhippo.model.vo.ProductImageVo
import org.springframework.stereotype.Component

@Component
open class ProductImageMapper {
    open fun mapTopProductImage(productImageVo: ProductImageVo, product: Product): ProductImage {
        val productImage = ProductImage()
        productImage.url = productImageVo.url
        productImage.filename = productImageVo.filename
        productImage.filesize = productImageVo.filesize
        productImage.height = productImageVo.height
        productImage.width = productImageVo.width
        productImage.mimeType = productImageVo.mimeType
        productImage.fileType = ProductFileType.fromValue(productImageVo.fileType)
        productImage.product = product

        return productImage
    }
}