package com.hanaro.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanaro.orders.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersQDslRepository {
}
