package com.leopoldhsing.digitalhippo.payment.api;

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto;
import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.leopoldhsing.digitalhippo.payment.service.ProductStripeService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Internal APIs for Stripe Product Management", description = "APIs for internal microservice communication. External access is restricted.")
@RestController
@RequestMapping("/api/stripe/inner/product")
public class ProductApi {

    private final ProductStripeService productStripeService;

    public ProductApi(ProductStripeService productStripeService) {
        this.productStripeService = productStripeService;
    }

    @Operation(
            summary = "Create a product in Stripe",
            description = "Creates a new product in Stripe based on the provided product information."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Stripe exception occurred",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping
    public Product createStripeProduct(
            @Parameter(description = "Product information to create in Stripe", required = true)
            @RequestBody Product product
    ) {
        try {
            return productStripeService.createStripeProduct(product);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Update a product in Stripe",
            description = "Updates an existing product in Stripe with the provided product information."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Stripe exception occurred",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PutMapping
    public Product updateStripeProduct(
            @Parameter(description = "Product information to update in Stripe", required = true)
            @RequestBody Product product
    ) {
        try {
            return productStripeService.updateStripeProduct(product);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Delete a product from Stripe",
            description = "Deletes a product from Stripe using the provided Stripe product ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Stripe exception occurred",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @DeleteMapping("/{stripeId}")
    public Boolean deleteStripeProduct(
            @Parameter(description = "Stripe product ID to delete", required = true, example = "prod_ABC123")
            @PathVariable String stripeId
    ) {
        try {
            return productStripeService.deleteStripeProduct(stripeId);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
