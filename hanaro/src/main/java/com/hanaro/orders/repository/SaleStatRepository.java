package com.hanaro.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanaro.orders.entity.SaleStat;

public interface SaleStatRepository extends JpaRepository<SaleStat, String> {
}
