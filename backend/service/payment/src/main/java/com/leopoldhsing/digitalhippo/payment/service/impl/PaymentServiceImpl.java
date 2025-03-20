package com.leopoldhsing.digitalhippo.payment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leopoldhsing.digitalhippo.common.constants.StripeConstants;
import com.leopoldhsing.digitalhippo.common.exception.StripeSignatureInvalidException;
import com.leopoldhsing.digitalhippo.common.utils.RequestUtil;
import com.leopoldhsing.digitalhippo.feign.product.ProductFeignClient;
import com.leopoldhsing.digitalhippo.feign.user.UserFeignClient;
import com.leopoldhsing.digitalhippo.model.dto.SnsMessageDto;
import com.leopoldhsing.digitalhippo.model.entity.Order;
import com.leopoldhsing.digitalhippo.model.entity.ProductImage;
import com.leopoldhsing.digitalhippo.model.enumeration.NotificationType;
import com.leopoldhsing.digitalhippo.payment.config.StripeProperties;
import com.leopoldhsing.digitalhippo.payment.config.StripeSnsTopicProperties;
import com.leopoldhsing.digitalhippo.payment.service.OrderService;
import com.leopoldhsing.digitalhippo.payment.service.PaymentService;
import com.leopoldhsing.digitalhippo.payment.service.ProductStripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import io.awspring.cloud.sns.core.SnsTemplate;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final StripeProperties stripeProperties;
    private final ProductFeignClient productFeignClient;
    private final ProductStripeService productStripeService;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private final StripeSnsTopicProperties stripeSnsTopicProperties;
    private final SnsClient snsClient;

    public PaymentServiceImpl(StripeProperties stripeProperties, ProductFeignClient productFeignClient, ProductStripeService productStripeService, OrderService orderService, SnsTemplate snsTemplate, StripeSnsTopicProperties stripeSnsTopicProperties, UserFeignClient userFeignClient, SnsClient snsClient) {
        this.stripeProperties = stripeProperties;
        this.productFeignClient = productFeignClient;
        this.productStripeService = productStripeService;
        this.orderService = orderService;
        this.stripeSnsTopicProperties = stripeSnsTopicProperties;
        this.snsClient = snsClient;
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

            // 2. construct email params
            SnsMessageDto snsMessageDto = new SnsMessageDto();
            snsMessageDto.setType(NotificationType.RECEIPT);
            snsMessageDto.setEmail(order.getUser().getEmail());
            snsMessageDto.setOrderPayloadId(order.getPayloadId());
            // filter product images
            List<com.leopoldhsing.digitalhippo.model.entity.Product> orderProducts = order.getProducts();
            orderProducts.forEach(product -> {
                List<ProductImage> productImages = product.getProductImages().stream().filter(productImage -> "TABLET".equalsIgnoreCase(productImage.getFileType().getValue())).toList();
                product.setProductImages(productImages);
            });
            snsMessageDto.setProducts(orderProducts);

            // 3. send receipt email
            sendSnsNotification(snsMessageDto, "Receipt Email");
            log.info("Receipt email sent, email params: {}", snsMessageDto);
        }
    }

    // handle payment failed
    private void handlePaymentFailure(Event event) {
        var paymentIntent = (com.stripe.model.PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);

        if (paymentIntent != null) {
            String orderId = paymentIntent.getMetadata().get(StripeConstants.ORDER_ID);
        }
    }

    // send notification
    private void sendSnsNotification(SnsMessageDto snsMessageDto, String subject) {
        try {
            // Convert snsMessageDto to JSON string
            String message = objectMapper.writeValueAsString(snsMessageDto);

            // Construct PublishRequest
            PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(stripeSnsTopicProperties.getArn())
                    .message(message)
                    .subject(subject)
                    .messageAttributes(Map.of(
                            "type", MessageAttributeValue.builder()
                                    .dataType("String")
                                    .stringValue(String.valueOf(NotificationType.RECEIPT))
                                    .build()
                    ))
                    .build();

            // Send message
            PublishResponse response = snsClient.publish(publishRequest);
            log.info("Receipt email SQS Message ID: {}", response.messageId());

        } catch (Exception e) {
            log.error("Failed to send SNS notification", e);
            throw new RuntimeException("Failed to send SNS notification", e);
        }
    }
}
