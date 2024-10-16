package com.leopoldhsing.digitalhippo.feign.cart;

import com.leopoldhsing.digitalhippo.model.dto.AddToCartDto;
import com.leopoldhsing.digitalhippo.model.entity.Cart;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

@Component
public class CartFeignClientFallback implements CartFeignClient {
    @Override
    public void addItem(@RequestBody AddToCartDto addToCartDto) {

    }

    @Override
    public Collection<Cart> getItems(Long userId) {
        return List.of();
    }

    @Override
    public void clearUserCartItems() {

    }

    @Override
    public void clearUserCartItems(Long userId) {

    }
}
