package com.leopoldhsing.digitalhippo.stripe.service;

import com.leopoldhsing.digitalhippo.model.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(List<Long> productIdList, Long userId);
}
