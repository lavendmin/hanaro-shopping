package com.hanaro.cart.dto;

import com.hanaro.item.dto.ItemDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponseDTO {
	private long cartId;
	private int quantity;
	private ItemDTO item;
}
