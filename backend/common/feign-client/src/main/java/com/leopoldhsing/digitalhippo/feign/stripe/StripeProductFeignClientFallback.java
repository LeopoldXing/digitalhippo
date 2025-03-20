package com.leopoldhsing.digitalhippo.feign.stripe;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class StripeProductFeignClientFallback implements StripeProductFeignClient {
    @Override
    public Product createStripeProduct(Product product) {
        return new Product();
    }

    @Override
    public Product updateStripeProduct(Product product) {
        return null;
    }

    @Override
    public Boolean deleteStripeProduct(String stripeId) {
        return false;
    }
}
