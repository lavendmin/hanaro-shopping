package com.hanaro.orders.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrderItemDTO {
	private Long orderId;
	private Long itemId;
	private String itemName;
	private int quantity;
	private int discountedPrice;
	private double discount;
	private int originalPrice;
}
