package com.hanaro.cart.dto;

import com.hanaro.item.dto.ItemDTO;

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
public class CartResponseDTO {
	private long cartId;
	private int quantity;
	private ItemDTO item;
}
