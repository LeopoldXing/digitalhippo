package com.leopoldhsing.digitalhippo.product.service.impl

import com.leopoldhsing.digitalhippo.common.exception.AuthenticationFailedException
import com.leopoldhsing.digitalhippo.common.exception.ResourceNotFoundException
import com.leopoldhsing.digitalhippo.feign.user.UserFeignClient
import com.leopoldhsing.digitalhippo.model.dto.ProductSearchingConditionDto
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole
import com.leopoldhsing.digitalhippo.product.repository.ProductImageRepository
import com.leopoldhsing.digitalhippo.product.repository.ProductRepository
import com.leopoldhsing.digitalhippo.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl @Autowired constructor(
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val userFeignClient: UserFeignClient
) : ProductService {

    override fun conditionalSearchProducts(condition: ProductSearchingConditionDto): List<Product> {
        TODO("Not yet implemented")
    }

    override fun getProduct(productId: Long): Product {
        val product = productRepository.findByIdOrNull(productId)
        if (product == null) {
            throw ResourceNotFoundException("product", "id", productId.toString())
        }
        return product
    }

    override fun createProduct(product: Product): Product {
        // 1. get user
        val currentUser = userFeignClient.getCurrentUser()
        product.user = currentUser

        // 2. save product
        productRepository.save(product)

        return product
    }

    override fun updateProduct(product: Product): Product {
        // 1. get user
        val currentUser = userFeignClient.currentUser

        // 2. find the product
        val originalProduct = productRepository.findProductByPayloadId(product.payloadId)
        if (originalProduct == null) {
            return createProduct(product)
        }

        // 3. determine if the user has the authority to update this product
        if (currentUser != null && (currentUser.role === UserRole.ADMIN || currentUser.id == originalProduct.user.id)) {
            // user has the authority
            // 4. update product
            product.id = originalProduct.id
            product.user = originalProduct.user

            // 4.1 update images
            productImageRepository.deleteProductImageByIdIn(product.productImages.map { image -> image.id })

            productRepository.save(product)
        } else {
            // user does not have the authority
            throw AuthenticationFailedException(currentUser.id.toString(), currentUser.email)
        }

        return Product()
    }

    override fun deleteProduct(payloadId: String) {
        // 1. get user
        val currentUser = userFeignClient.getCurrentUser()

        // 2. find the product
        val product = productRepository.findProductByPayloadId(payloadId)

        if (product != null) {
            // 3. determine if the user has the authority to delete this product
            if (currentUser.role === UserRole.ADMIN || product.user?.id == currentUser.id) {
                // current user has authority
                productRepository.delete(product)
            } else {
                // current user does not have authority
                throw AuthenticationFailedException(currentUser.id.toString(), currentUser.email)
            }
        }
    }
}