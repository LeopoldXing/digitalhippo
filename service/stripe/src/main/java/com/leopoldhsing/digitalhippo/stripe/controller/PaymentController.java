package com.leopoldhsing.digitalhippo.stripe.controller;

import com.leopoldhsing.digitalhippo.common.constants.StripeConstants;
import com.leopoldhsing.digitalhippo.model.vo.payment.CreateCheckoutSessionVo;
import com.leopoldhsing.digitalhippo.stripe.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/stripe/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/checkout-session")
    public ResponseEntity<String> createCheckoutSession(@RequestBody CreateCheckoutSessionVo createCheckoutSessionVo) {
        String sessionUrl = paymentService
                .createCheckoutSession(createCheckoutSessionVo.getPayloadOrderId(), createCheckoutSessionVo.getProductIdList());

        return ResponseEntity.ok(sessionUrl);
    }

    @PostMapping("/confirm")
    public void confirmPayment(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        String responseCode = paymentService.confirmPayment(payload, sigHeader);

        if (StripeConstants.PAYMENT_SUCCESS.equalsIgnoreCase(responseCode)) {
            // log payment result as successful
            log.info("payment confirmed, payload: {}", payload);
        } else {
            // log payment result failed
            log.info("payment failed, payload: {}", payload);
        }
    }
}
