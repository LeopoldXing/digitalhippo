package com.leopoldhsing.digitalhippo.stripe.service;

import java.util.List;

public interface PaymentService {
    String createCheckoutSession(String payloadOrderId, List<Long> productIdList);
}
