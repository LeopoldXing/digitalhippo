package com.leopoldhsing.digitalhippo.payment.repository;

import com.leopoldhsing.digitalhippo.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order getOrderById(Long orderId);
}
