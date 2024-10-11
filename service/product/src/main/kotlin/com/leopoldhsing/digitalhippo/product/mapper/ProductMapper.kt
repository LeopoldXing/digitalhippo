package com.leopoldhsing.digitalhippo.product.mapper

import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.model.vo.ProductVo
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Component

@Component
class ProductMapper private constructor() {

    companion object {
        fun mapToProduct(productVo: ProductVo, approvedForSale: String = "pending"): Product {
            val product = Product()
            product.payloadId = productVo.payloadId
            product.name = productVo.filename
            product.description = productVo.description
            product.price = productVo.price
            product.productFileUrl = productVo.productFileUrl
            product.category = productVo.category
            product.approvedForSale = approvedForSale
            val productImageList =
                productVo.productImages?.map { productImageVo -> ProductImageMapper.mapTopProductImage(productImageVo, product) }
            product.productImages = productImageList

            return product
        }

        fun mapToIndex(product: Product): ProductIndex {
            val productIndex = ProductIndex()
            BeanUtils.copyProperties(product, productIndex)
            productIndex.sellerEmail = product.user.email

            return productIndex
        }
    }
}