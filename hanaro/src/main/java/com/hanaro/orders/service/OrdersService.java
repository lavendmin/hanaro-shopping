package com.hanaro.orders.service;

import org.springframework.data.domain.Page;

import com.hanaro.PageCond;
import com.hanaro.SearchOrdersCond;
import com.hanaro.member.entity.Member;
import com.hanaro.orders.dto.OrderDTO;

public interface OrdersService {
	OrderDTO createOrder(Member member);

	Page<OrderDTO> getMyOrders(Member member, PageCond pageCond);

	Page<OrderDTO> getOrders(SearchOrdersCond searchOrdersCond);

}
