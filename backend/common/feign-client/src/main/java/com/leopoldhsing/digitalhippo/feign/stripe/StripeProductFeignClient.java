package com.leopoldhsing.digitalhippo.feign.stripe;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "stripe", fallback = StripeProductFeignClientFallback.class)
public interface StripeProductFeignClient {

    @PostMapping("/api/stripe/inner/product")
    Product createStripeProduct(@RequestBody Product product);

    @PutMapping("/api/stripe/inner/product")
    Product updateStripeProduct(@RequestBody Product product);

    @DeleteMapping("/api/stripe/inner/product/{stripeId}")
    Boolean deleteStripeProduct(@PathVariable String stripeId);
}
