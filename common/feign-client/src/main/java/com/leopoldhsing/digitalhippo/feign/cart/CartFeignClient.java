package com.leopoldhsing.digitalhippo.feign.cart;

import com.leopoldhsing.digitalhippo.model.dto.AddToCartDto;
import com.leopoldhsing.digitalhippo.model.entity.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@FeignClient(name = "cart", fallback = CartFeignClientFallback.class)
public interface CartFeignClient {

    @PostMapping("/api/cart/inner")
    void addItem(@RequestBody AddToCartDto addToCartDto);

    @GetMapping("/api/cart/inner/{userId}")
    Collection<Cart> getItems(@PathVariable Long userId);
}
