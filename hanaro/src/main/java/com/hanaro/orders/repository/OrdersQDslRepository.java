package com.hanaro.orders.repository;

import org.springframework.data.domain.Page;

import com.hanaro.SearchOrdersCond;
import com.hanaro.orders.entity.Orders;

public interface OrdersQDslRepository {
	Page<Orders> search(SearchOrdersCond searchOrdersCond);
}
