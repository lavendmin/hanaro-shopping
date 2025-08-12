package com.hanaro.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanaro.orders.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
