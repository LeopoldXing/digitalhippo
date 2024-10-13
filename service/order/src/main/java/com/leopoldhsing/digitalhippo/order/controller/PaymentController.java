package com.leopoldhsing.digitalhippo.order.controller;

import com.leopoldhsing.digitalhippo.order.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/checkout-session")
    public void createCheckoutSession() {
        String sessionUrl = paymentService.createCheckoutSession();
    }

}
