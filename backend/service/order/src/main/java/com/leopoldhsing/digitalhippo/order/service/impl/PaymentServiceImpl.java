package com.leopoldhsing.digitalhippo.order.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leopoldhsing.digitalhippo.common.constants.StripeConstants;
import com.leopoldhsing.digitalhippo.common.exception.StripeSignatureInvalidException;
import com.leopoldhsing.digitalhippo.common.utils.RequestUtil;
import com.leopoldhsing.digitalhippo.feign.product.ProductFeignClient;
import com.leopoldhsing.digitalhippo.model.entity.Order;
import com.leopoldhsing.digitalhippo.order.config.StripeProperties;
import com.leopoldhsing.digitalhippo.order.service.OrderService;
import com.leopoldhsing.digitalhippo.order.service.PaymentService;
import com.leopoldhsing.digitalhippo.order.service.ProductStripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final StripeProperties stripeProperties;
    private final ProductFeignClient productFeignClient;
    private final ProductStripeService productStripeService;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    public PaymentServiceImpl(StripeProperties stripeProperties, ProductFeignClient productFeignClient, ProductStripeService productStripeService, OrderService orderService) {
        this.stripeProperties = stripeProperties;
        this.productFeignClient = productFeignClient;
        this.productStripeService = productStripeService;
        this.orderService = orderService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void configureStripeClient() {
        Stripe.apiKey = stripeProperties.getSecretKey();
    }

    @Override
    public String createCheckoutSession(String payloadOrderId, List<Long> productIdList) {
        // 1. create order
        Long uid = RequestUtil.getUid();
        Order order = orderService.createOrder(payloadOrderId, productIdList, uid);

        // 2. get stripe id list
        List<String> stripeIdList = productFeignClient.getStripeIdList(productIdList);

        // 3. get stripeProduct
        List<Product> stripeProductList;
        try {
            stripeProductList = productStripeService.getStripeProducts(stripeIdList);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        // 4. build line items
        List<SessionCreateParams.LineItem> lineItems = stripeProductList.stream()
                .map(product -> SessionCreateParams.LineItem.builder()
                        .setQuantity(1L) // all the products have the quantity of 1
                        .setPrice(product.getDefaultPrice())
                        .build())
                .toList();

        // 5. build metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put(StripeConstants.ORDER_ID, String.valueOf(order.getId()));

        // 6. build create checkout session params
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                // payment method
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.ALIPAY)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.WECHAT_PAY)
                // wechat payment configuration
                .setPaymentMethodOptions(
                        SessionCreateParams.PaymentMethodOptions.builder()
                                .setWechatPay(
                                        SessionCreateParams.PaymentMethodOptions.WechatPay.builder()
                                                .setClient(SessionCreateParams.PaymentMethodOptions.WechatPay.Client.WEB)
                                                .build()
                                ).build()
                )
                // metadata
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putAllMetadata(metadata)
                                .build()
                )
                // success url
                .setSuccessUrl(stripeProperties.getFrontendEndpoint() + "/thank-you?orderId=" + order.getId())
                // cancel url
                .setCancelUrl(stripeProperties.getFrontendEndpoint() + "/checkout?loggedIn=true")
                // line items
                .addAllLineItem(lineItems)
                .build();

        // 7. create checkout session
        Session session;
        try {
            session = Session.create(params);
            log.info("checkout session created, session: {}, orderId: {}", session, metadata.get(StripeConstants.ORDER_ID));
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        // 8. return url
        return session.getUrl();
    }

    @Override
    public String confirmPayment(String payload, String sigHeader) {
        Event event;
        try {
            // 1. verify the signature, make sure this request comes from stripe
            event = Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            // signature verified failed, return 400
            log.error("signature verification failed", e);
            throw new StripeSignatureInvalidException();
        }

        log.info("signature verified, event: {}", event);

        // 2. handle payment result & return response code
        return switch (event.getType()) {
            case "payment_intent.succeeded" -> {
                // payment successful
                handlePaymentSuccess(event);
                yield StripeConstants.PAYMENT_SUCCESS;
            }
            case "payment_intent.payment_failed" -> {
                // payment failed
                log.error("payment failed, paymentInfo: {}", event);
                handlePaymentFailure(event);
                yield StripeConstants.PAYMENT_FAILED;
            }
            // something else happened
            default -> StripeConstants.PAYMENT_ERROR;
        };
    }

    // handle case payment successful
    public void handlePaymentSuccess(Event event) {
        var paymentIntent = (com.stripe.model.PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);

        if (paymentIntent != null) {
            String orderId = paymentIntent.getMetadata().get(StripeConstants.ORDER_ID);

            log.info("payment successful, paymentInfo: {}, orderId: {}", event, orderId);

            // 1. update order status in postgres database
            Order order = orderService.updateOrderStatus(Long.valueOf(orderId), true);
        }
    }

    // handle payment failed
    private void handlePaymentFailure(Event event) {
        var paymentIntent = (com.stripe.model.PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);

        if (paymentIntent != null) {
            String orderId = paymentIntent.getMetadata().get(StripeConstants.ORDER_ID);
        }
    }
}
