package com.leopoldhsing.digitalhippo.stripe.service.impl;

import com.leopoldhsing.digitalhippo.model.entity.Order;
import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.leopoldhsing.digitalhippo.model.entity.User;
import com.leopoldhsing.digitalhippo.stripe.repository.OrderRepository;
import com.leopoldhsing.digitalhippo.stripe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(String payloadId, List<Long> productIdList, Long userId) {
        User user = new User();
        user.setId(userId);

        List<Product> productList = productIdList.stream().map(productId -> {
            Product product = new Product();
            product.setId(productId);
            return product;
        }).toList();
        return orderRepository.save(new Order(payloadId, user, productList, false));
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.getOrderById(orderId);
    }
}
