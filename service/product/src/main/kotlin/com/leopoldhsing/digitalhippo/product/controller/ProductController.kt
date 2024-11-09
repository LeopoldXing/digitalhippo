package com.leopoldhsing.digitalhippo.product.controller

import com.leopoldhsing.digitalhippo.common.mapper.product.ProductMapper
import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.model.dto.SearchingResultDto
import com.leopoldhsing.digitalhippo.model.entity.Product
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo
import com.leopoldhsing.digitalhippo.model.vo.ProductVo
import com.leopoldhsing.digitalhippo.product.service.ProductService
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Tag(name = "REST APIs for Product Management", description = "REST APIs for managing products (CREATE, READ, UPDATE, DELETE)")
@Controller
@RequestMapping("/api/product")
class ProductController @Autowired constructor(
    private val productService: ProductService
) {

    /**
     * Conditional search for products
     */
    @Operation(
        summary = "Search products based on conditions",
        description = "Retrieve a list of products that match the given search conditions."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Products retrieved successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = SearchingResultDto::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @GetMapping("/search")
    fun searchProduct(
        @Parameter(description = "Search conditions", required = false)
        @ModelAttribute condition: ProductSearchingConditionVo
    ): ResponseEntity<SearchingResultDto> {
        val searchingResultDto = productService.conditionalSearchProducts(condition)
        return ResponseEntity.ok(searchingResultDto)
    }

    /**
     * Get product by ID
     */
    @Operation(
        summary = "Get product details",
        description = "Retrieve detailed information of a product by its ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Product retrieved successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ProductVo::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Product not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @GetMapping("/{productId}")
    fun getProduct(
        @Parameter(description = "ID of the product to retrieve", example = "1")
        @PathVariable productId: Long
    ): ResponseEntity<ProductVo> {
        val product = productService.getProduct(productId)
        val productVo: ProductVo = ProductMapper.mapToProductVo(product)
        return ResponseEntity.ok(productVo)
    }

    /**
     * Create a new product
     */
    @Operation(
        summary = "Create a new product",
        description = "Add a new product to the system."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Product created successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = Product::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PostMapping
    fun createProduct(
        @Parameter(description = "Product information", required = true)
        @RequestBody productVo: ProductVo
    ): ResponseEntity<Product> {
        val newProduct = productService.createProduct(ProductMapper.mapToProduct(productVo, "pending"))
        return ResponseEntity.status(201).body(newProduct)
    }

    /**
     * Delete a product by ID
     */
    @Operation(
        summary = "Delete a product",
        description = "Remove a product from the system by its payload ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Product deleted successfully"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Product not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @DeleteMapping("/{payloadId}")
    fun deleteProduct(
        @Parameter(description = "Payload ID of the product to delete", example = "payload123")
        @PathVariable payloadId: String
    ): ResponseEntity<Void> {
        productService.deleteProduct(payloadId)
        return ResponseEntity.noContent().build()
    }

    /**
     * Update an existing product
     */
    @Operation(
        summary = "Update a product",
        description = "Modify the details of an existing product."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Product updated successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = Product::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Product not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PutMapping
    fun updateProduct(
        @Parameter(description = "Updated product information", required = true)
        @RequestBody productVo: ProductVo
    ): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(ProductMapper.mapToProduct(productVo, "pending"))
        return ResponseEntity.ok(updatedProduct)
    }
}
