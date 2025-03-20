package com.leopoldhsing.digitalhippo.stripe.service.impl;

import com.leopoldhsing.digitalhippo.feign.cart.CartFeignClient;
import com.leopoldhsing.digitalhippo.model.entity.Order;
import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.leopoldhsing.digitalhippo.model.entity.User;
import com.leopoldhsing.digitalhippo.stripe.repository.OrderRepository;
import com.leopoldhsing.digitalhippo.stripe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartFeignClient cartFeignClient;

    @Override
    public Order createOrder(String payloadId, List<Long> productIdList, Long userId) {
        User user = new User();
        user.setId(userId);
        // 1. fetch product list
        CompletableFuture<List<Product>> productListFuture = CompletableFuture
                .supplyAsync(() -> productIdList.stream().map(productId -> {
                    Product product = new Product();
                    product.setId(productId);
                    return product;
                }).toList());

        // 2. clear user's cart
        CompletableFuture<Void> clearCartFuture = CompletableFuture
                .runAsync(() -> cartFeignClient.clearUserCartItems(userId));

        // 3. create order and save to database
        return CompletableFuture
                .allOf(productListFuture, clearCartFuture)
                .thenApply(v -> {
                    Order order;
                    try {
                        order = orderRepository.save(new Order(payloadId, user, productListFuture.get(), false));
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    return order;
                })
                .join();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.getOrderById(orderId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, Boolean isPaid) {
        Order order = orderRepository.getOrderById(orderId);
        order.setIsPaid(isPaid);
        return orderRepository.save(order);
    }
}
