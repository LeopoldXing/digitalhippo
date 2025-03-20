package com.leopoldhsing.digitalhippo.stripe.controller;

import com.leopoldhsing.digitalhippo.common.constants.StripeConstants;
import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto;
import com.leopoldhsing.digitalhippo.model.vo.payment.CreateCheckoutSessionVo;
import com.leopoldhsing.digitalhippo.stripe.service.PaymentService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "REST APIs for Payment Management", description = "REST APIs for handling payments with Stripe")
@Slf4j
@RestController
@RequestMapping("/api/stripe/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(
            summary = "Create a Stripe Checkout Session",
            description = "Creates a Stripe Checkout Session for processing payments."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Checkout session created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping("/checkout-session")
    public ResponseEntity<String> createCheckoutSession(
            @Parameter(description = "Checkout session details", required = true)
            @RequestBody CreateCheckoutSessionVo createCheckoutSessionVo
    ) {
        String sessionUrl = paymentService
                .createCheckoutSession(createCheckoutSessionVo.getPayloadOrderId(), createCheckoutSessionVo.getProductIdList());

        return ResponseEntity.ok(sessionUrl);
    }

    @Operation(
            summary = "Confirm payment",
            description = "Confirms the payment by handling the Stripe webhook event."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment confirmed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid Stripe signature",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping("/confirm")
    public void confirmPayment(
            @Parameter(description = "Stripe webhook payload", required = true)
            @RequestBody String payload,
            @Parameter(description = "Stripe signature header", required = true)
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        String responseCode = paymentService.confirmPayment(payload, sigHeader);

        if (StripeConstants.PAYMENT_SUCCESS.equalsIgnoreCase(responseCode)) {
            // log payment result as successful
            log.info("Payment confirmed, payload: {}", payload);
        } else {
            // log payment result failed
            log.info("Payment failed, payload: {}", payload);
        }
    }
}
