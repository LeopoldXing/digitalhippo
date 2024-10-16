package com.leopoldhsing.digitalhippo.product.controller

import com.leopoldhsing.digitalhippo.common.mapper.product.ProductMapper
import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.model.dto.SearchingResultDto
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo
import com.leopoldhsing.digitalhippo.model.vo.ProductVo
import com.leopoldhsing.digitalhippo.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Tag(name = "CRUD REST APIs for Products", description = "REST APIs to CREATE / UPDATE / FETCH and DELETE products")
@Controller
@RequestMapping("/api/product")
class ProductController @Autowired constructor(
    private val productService: ProductService
) {

    /**
     * conditional search products
     */
    @Operation(summary = "Search all products based on input conditions")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Searching product successful"),
            ApiResponse(
                responseCode = "500",
                description = "Failed to search product",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
        ]
    )
    @GetMapping("/search")
    fun searchProduct(@ModelAttribute condition: ProductSearchingConditionVo): ResponseEntity<SearchingResultDto> {
        val searchingResultDto = productService.conditionalSearchProducts(condition)

        return ResponseEntity.ok(searchingResultDto)
    }

    /**
     * endpoint for getting product by id
     */
    @Operation(summary = "Get product by Id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product queried successful"),
            ApiResponse(
                responseCode = "404",
                description = "Product not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
        ]
    )
    @GetMapping("/{productId}")
    fun getProduct(@PathVariable productId: Long): ResponseEntity<ProductVo> {
        val product = productService.getProduct(productId)
        val productVo: ProductVo = ProductMapper.mapToProductVo(product)

        return ResponseEntity.ok(productVo)
    }

    /**
     * endpoint for creating new product
     */
    @Operation(summary = "Create new product")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product created successful"),
            ApiResponse(
                responseCode = "400",
                description = "User not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
        ]
    )
    @PostMapping
    fun createProduct(@RequestBody productVo: ProductVo): ResponseEntity<Product> {
        val newProduct = productService.createProduct(ProductMapper.mapToProduct(productVo, "pending"))
        return ResponseEntity.ok(newProduct)
    }

    /**
     * endpoint for deleting product by id
     */
    @Operation(summary = "Delete product")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "Product deleted successful")])
    @DeleteMapping("/{payloadId}")
    fun deleteProduct(@PathVariable payloadId: String): ResponseEntity<Void> {
        productService.deleteProduct(payloadId)
        return ResponseEntity.ok().build()
    }

    /**
     * endpoint for updating product
     */
    @Operation(summary = "Update product")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product updated successful"),
            ApiResponse(
                responseCode = "400",
                description = "Product not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
        ]
    )
    @PutMapping
    fun updateProduct(@RequestBody productVo: ProductVo): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(ProductMapper.mapToProduct(productVo, "pending"))
        return ResponseEntity.ok(updatedProduct)
    }
}