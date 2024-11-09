package com.leopoldhsing.digitalhippo.product.api

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto
import com.leopoldhsing.digitalhippo.product.service.ProductService
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.media.*
import io.swagger.v3.oas.annotations.responses.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Internal Product APIs",
    description = "APIs for internal microservice communication. External access is restricted."
)
@RestController
@RequestMapping("/api/product/inner")
open class ProductApi @Autowired constructor(private val productService: ProductService) {

    @Operation(
        summary = "Retrieve Stripe IDs for a list of product IDs",
        description = "Given a list of product IDs, return the corresponding Stripe product IDs."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Stripe IDs retrieved successfully",
                content = [Content(mediaType = "application/json", array = ArraySchema(schema = Schema(type = "string")))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "One or more products not found",
                content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
            )
        ]
    )
    @PostMapping("/stripe-ids")
    open fun getStripeIdList(
        @Parameter(description = "List of product IDs", required = true)
        @RequestBody productIdList: List<Long>
    ): List<String> {
        val products = productService.getProducts(productIdList)
        val stripeIdList: List<String> = products.map { it.stripeId }
        return stripeIdList
    }
}
