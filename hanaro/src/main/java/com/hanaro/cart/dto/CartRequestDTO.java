package com.hanaro.cart.dto;

import jakarta.validation.constraints.Min;

public record CartRequestDTO(
	long itemId,

	@Min(1)
	int quantity
) {
}
