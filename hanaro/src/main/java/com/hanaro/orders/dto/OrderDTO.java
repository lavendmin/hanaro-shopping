package com.hanaro.orders.dto;

import java.util.List;

import com.hanaro.orders.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
	private OrderStatus orderStatus;
	private int totalAmount;
	private List<OrderItemDTO> orderItems;
}
