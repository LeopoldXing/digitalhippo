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
            BeanUtils.copyProperties(productVo, product)
            product.approvedForSale = approvedForSale
            val productImageList =
                productVo.productImages?.map { productImageVo -> ProductImageMapper.mapTopProductImage(productImageVo, product) }
            product.productImages = productImageList

            return product
        }

        fun mapToProduct(productIndex: ProductIndex): Product {
            val product = Product()
            BeanUtils.copyProperties(productIndex, product)

            return product
        }

        fun mapToIndex(product: Product): ProductIndex {
            val productIndex = ProductIndex()
            BeanUtils.copyProperties(product, productIndex)
            productIndex.sellerEmail = product.user.email
            productIndex.price = product.price.toDouble()
            productIndex.category = product.category.lowercase()
            return productIndex
        }
    }
}