package com.leopoldhsing.digitalhippo.order.service.impl;

import com.leopoldhsing.digitalhippo.order.config.StripeProperties;
import com.leopoldhsing.digitalhippo.order.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private StripeProperties stripeProperties;

    @Override
    public String createCheckoutSession() {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(stripeProperties.getCustomDomain() + "?success=true")
                        .setCancelUrl(stripeProperties.getCustomDomain() + "?canceled=true")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        // Provide the exact Price ID (for example, pr_1234) of the product you want to sell
                                        .setPrice("{{PRICE_ID}}")
                                        .build())
                        .build();
        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        return session.getSuccessUrl();
    }
}
