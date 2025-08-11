package com.hanaro.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanaro.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
