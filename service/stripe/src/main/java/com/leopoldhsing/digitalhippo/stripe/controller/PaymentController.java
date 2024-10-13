package com.leopoldhsing.digitalhippo.stripe.controller;

import com.leopoldhsing.digitalhippo.stripe.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stripe/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/checkout-session")
    public ResponseEntity<String> createCheckoutSession(@RequestBody List<Long> productIdList) {
        String sessionUrl = paymentService.createCheckoutSession(productIdList);

        return ResponseEntity.ok(sessionUrl);
    }

}
