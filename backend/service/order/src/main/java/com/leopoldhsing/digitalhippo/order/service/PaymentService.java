package com.leopoldhsing.digitalhippo.order.service;

import java.util.List;

public interface PaymentService {
    String createCheckoutSession(String payloadOrderId, List<Long> productIdList);

    String confirmPayment(String payload, String sigHeader);
}
