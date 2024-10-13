package com.leopoldhsing.digitalhippo.stripe.service;

import com.stripe.model.LineItem;

import java.util.ArrayList;
import java.util.List;

public interface PaymentService {
    String createCheckoutSession();

    default List<LineItem> buildLineItems() {
        return new ArrayList<>();
    }
}
