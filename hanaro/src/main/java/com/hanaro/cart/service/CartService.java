package com.hanaro.cart.service;

import com.hanaro.cart.dto.CartRequestDTO;
import com.hanaro.cart.dto.CartResponseDTO;
import com.hanaro.member.entity.Member;

import jakarta.validation.Valid;

public interface CartService {
	CartResponseDTO addItemToCart(Member member, @Valid CartRequestDTO cartRequestDTO);

	CartResponseDTO updateCart(Member member, @Valid CartRequestDTO cartRequestDTO);

	String deleteItemFromCart(Member member, Long itemId);
}
