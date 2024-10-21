package com.leopoldhsing.digitalhippo.product.api

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(
    name = "Products API endpoint for other microservices",
    description = "Private product APIs, external requests will be blocked directly"
)
@RestController
@RequestMapping("/api/product/inner")
open class ProductApi @Autowired constructor(private val productService: ProductService) {

    @Operation(summary = "Get the stripeId of a given product id list")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Stripe product id successfully queried"),
            ApiResponse(
                responseCode = "404",
                description = "Product id not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PostMapping
    open fun getStripeIdList(@RequestBody productIdList: List<Long>): List<String> {
        val products = productService.getProducts(productIdList)

        val stripeIdList: List<String> = products.map { it.stripeId }

        return stripeIdList
    }
}