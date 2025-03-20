package com.leopoldhsing.digitalhippo.stripe.service;

import com.leopoldhsing.digitalhippo.model.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(String payloadId, List<Long> productIdList, Long userId);

    Order getOrderById(Long orderId);

    Order updateOrderStatus(Long orderId, Boolean isPaid);
}
