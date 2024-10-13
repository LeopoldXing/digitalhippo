package com.leopoldhsing.digitalhippo.stripe.service.impl;

import com.leopoldhsing.digitalhippo.stripe.config.StripeProperties;
import com.leopoldhsing.digitalhippo.stripe.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final StripeProperties stripeProperties;

    public PaymentServiceImpl(StripeProperties stripeProperties) {
        this.stripeProperties = stripeProperties;
    }

    @PostConstruct
    public void configureStripeClient() {
        Stripe.apiKey = stripeProperties.getSecretKey();
    }

    @Override
    public String createCheckoutSession() {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                // payment method
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.ALIPAY)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.WECHAT_PAY)
                // success url
                .setSuccessUrl(stripeProperties.getFrontendEndpoint() + "?success=true")
                // cancel url
                .setCancelUrl(stripeProperties.getFrontendEndpoint() + "?canceled=true")
                // line items
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
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
