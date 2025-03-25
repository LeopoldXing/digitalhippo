package com.leopoldhsing.digitalhippo.payment.controller;

import com.leopoldhsing.digitalhippo.common.constants.StripeConstants;
import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto;
import com.leopoldhsing.digitalhippo.model.vo.payment.CreateCheckoutSessionVo;
import com.leopoldhsing.digitalhippo.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "REST APIs for Payment Management", description = "REST APIs for handling payments with Stripe")
@RestController
@RequestMapping("/api/stripe/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final Logger log = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

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
