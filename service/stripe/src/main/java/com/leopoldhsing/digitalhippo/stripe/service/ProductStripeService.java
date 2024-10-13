package com.leopoldhsing.digitalhippo.stripe.service;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.stripe.exception.StripeException;

public interface ProductStripeService {

    Product createStripeProduct(Product product) throws StripeException;

    Product updateStripeProduct(Product product) throws StripeException;

    Boolean deleteStripeProduct(String stripeId) throws StripeException;
}
