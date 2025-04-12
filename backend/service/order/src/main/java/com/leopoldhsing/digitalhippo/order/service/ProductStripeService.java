package com.leopoldhsing.digitalhippo.order.service;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.stripe.exception.StripeException;

import java.util.List;

public interface ProductStripeService {

    List<com.stripe.model.Product> getStripeProducts(List<String> stripeIdList) throws StripeException;

    Product createStripeProduct(Product product) throws StripeException;

    Product updateStripeProduct(Product product) throws StripeException;

    Boolean deleteStripeProduct(String stripeId) throws StripeException;
}
