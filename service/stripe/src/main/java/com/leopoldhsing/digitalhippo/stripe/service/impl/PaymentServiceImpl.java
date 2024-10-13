package com.leopoldhsing.digitalhippo.stripe.service.impl;

import com.leopoldhsing.digitalhippo.common.utils.RequestUtil;
import com.leopoldhsing.digitalhippo.feign.product.ProductFeignClient;
import com.leopoldhsing.digitalhippo.model.entity.Order;
import com.leopoldhsing.digitalhippo.stripe.config.StripeProperties;
import com.leopoldhsing.digitalhippo.stripe.service.OrderService;
import com.leopoldhsing.digitalhippo.stripe.service.PaymentService;
import com.leopoldhsing.digitalhippo.stripe.service.ProductStripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final StripeProperties stripeProperties;
    private final ProductFeignClient productFeignClient;
    private final ProductStripeService productStripeService;
    private final OrderService orderService;

    public PaymentServiceImpl(StripeProperties stripeProperties, ProductFeignClient productFeignClient, ProductStripeService productStripeService, OrderService orderService) {
        this.stripeProperties = stripeProperties;
        this.productFeignClient = productFeignClient;
        this.productStripeService = productStripeService;
        this.orderService = orderService;
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
                        .setQuantity(1L) // quantity is 1 for all products
                        .setPrice(product.getDefaultPrice())
                        .build())
                .toList();

        // 5. build create checkout session params
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
                // success url
                .setSuccessUrl(stripeProperties.getFrontendEndpoint() + "/thank-you?orderId=" + order.getId())
                // cancel url
                .setCancelUrl(stripeProperties.getFrontendEndpoint() + "/cart")
                // line items
                .addAllLineItem(lineItems)
                .build();

        // 6. create checkout session
        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        // 7. return url
        return session.getUrl();
    }
}
