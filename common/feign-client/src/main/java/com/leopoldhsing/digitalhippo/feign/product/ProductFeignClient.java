package com.leopoldhsing.digitalhippo.feign.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product", fallback = ProductFeignClientFallback.class)
public interface ProductFeignClient {

    @PostMapping("/api/product/inner")
    List<String> getStripeIdList(@RequestBody List<Long> productIdList);
}
