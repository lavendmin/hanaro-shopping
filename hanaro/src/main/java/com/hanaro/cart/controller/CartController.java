package com.hanaro.cart.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanaro.cart.dto.CartRequestDTO;
import com.hanaro.cart.dto.CartResponseDTO;
import com.hanaro.cart.service.CartService;
import com.hanaro.member.entity.Member;
import com.hanaro.member.service.MemberService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Tag(name = "Cart", description = "장바구니 담기/수정/삭제")
public class CartController {
	private final CartService cartService;
	private final MemberService memberService;

	@Tag(name = "장바구니 담기")
	@PostMapping("/items")
	public ResponseEntity<?> addItemToCart(@Valid @RequestBody CartRequestDTO cartRequestDTO) {
		// long memberId = memberService.getMemberId();
		Member member = memberService.getLoggedInMember();

		CartResponseDTO cartResponseDTO = cartService.addItemToCart(member, cartRequestDTO);
		return ResponseEntity.ok(cartResponseDTO);
	}

	@Tag(name = "장바구니 수정")
	@PatchMapping("/items")
	public ResponseEntity<?> updateCart(@Valid @RequestBody CartRequestDTO cartRequestDTO) {
		Member member = memberService.getLoggedInMember();

		CartResponseDTO cartResponseDTO = cartService.updateCart(member, cartRequestDTO);
		return ResponseEntity.ok(cartResponseDTO);
	}

	@Tag(name = "장바구니 삭제")
	@DeleteMapping("/items/{id}")
	public ResponseEntity<?> deleteItemFromCart(@PathVariable Long id) {
		Member member = memberService.getLoggedInMember();

		String message = cartService.deleteItemFromCart(member, id);
		return ResponseEntity.ok(Map.of("delete item from cart", message));
	}
}
