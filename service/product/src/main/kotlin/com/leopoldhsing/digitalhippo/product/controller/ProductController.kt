package com.leopoldhsing.digitalhippo.product.controller

import com.leopoldhsing.digitalhippo.model.dto.ProductSearchingConditionDto
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/product")
class ProductController @Autowired constructor(private val productService: ProductService) {

    @GetMapping
    fun searchProduct(@RequestParam condition: ProductSearchingConditionDto): ResponseEntity<List<Product>> {
        val productList = productService.conditionalSearchProducts(condition)

        return ResponseEntity.ok(productList)
    }

    /**
     * endpoint for getting product by id
     */
    @GetMapping("/{productId}")
    fun getProduct(@PathVariable productId: Long): ResponseEntity<Product> {
        val product = productService.getProduct(productId)
        return ResponseEntity.ok(product)
    }

    /**
     * endpoint for creating new product
     */
    @PostMapping
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val newProduct = productService.createProduct(product)
        return ResponseEntity.ok(newProduct)
    }

    /**
     * endpoint for deleting product by id
     */
    @DeleteMapping("/{productId}")
    fun deleteProduct(@PathVariable("productId") productId: Long): ResponseEntity<Void> {
        productService.deleteProduct(productId)
        return ResponseEntity.ok().build()
    }

    /**
     * endpoint for updating product
     */
    @PutMapping
    fun updateProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(product)
        return ResponseEntity.ok(updatedProduct)
    }
}