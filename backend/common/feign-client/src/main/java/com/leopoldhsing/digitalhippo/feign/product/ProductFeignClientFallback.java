package com.leopoldhsing.digitalhippo.feign.product;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductFeignClientFallback implements ProductFeignClient {
    @Override
    public List<String> getStripeIdList(List<Long> productIdList) {
        return List.of();
    }
}
