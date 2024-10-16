package com.leopoldhsing.digitalhippo.product.controller

import com.leopoldhsing.digitalhippo.common.mapper.product.ProductMapper
import com.leopoldhsing.digitalhippo.model.dto.SearchingResultDto
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo
import com.leopoldhsing.digitalhippo.model.vo.ProductVo
import com.leopoldhsing.digitalhippo.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/product")
class ProductController @Autowired constructor(
    private val productService: ProductService
) {

    @GetMapping("/search")
    fun searchProduct(@ModelAttribute condition: ProductSearchingConditionVo): ResponseEntity<SearchingResultDto> {
        val searchingResultDto = productService.conditionalSearchProducts(condition)

        return ResponseEntity.ok(searchingResultDto)
    }

    /**
     * endpoint for getting product by id
     */
    @GetMapping("/{productId}")
    fun getProduct(@PathVariable productId: Long): ResponseEntity<ProductVo> {
        val product = productService.getProduct(productId)
        val productVo: ProductVo = ProductMapper.mapToProductVo(product)

        return ResponseEntity.ok(productVo)
    }

    /**
     * endpoint for creating new product
     */
    @PostMapping
    fun createProduct(@RequestBody productVo: ProductVo): ResponseEntity<Product> {
        val newProduct = productService.createProduct(ProductMapper.mapToProduct(productVo, "pending"))
        return ResponseEntity.ok(newProduct)
    }

    /**
     * endpoint for deleting product by id
     */
    @DeleteMapping("/{payloadId}")
    fun deleteProduct(@PathVariable payloadId: String): ResponseEntity<Void> {
        productService.deleteProduct(payloadId)
        return ResponseEntity.ok().build()
    }

    /**
     * endpoint for updating product
     */
    @PutMapping
    fun updateProduct(@RequestBody productVo: ProductVo): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(ProductMapper.mapToProduct(productVo, "pending"))
        return ResponseEntity.ok(updatedProduct)
    }
}