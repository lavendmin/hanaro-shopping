package com.hanaro.cart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Tag(name = "Cart", description = "장바구니 담기/수정/삭제/조회")
public class CartController {
	private final CartService cartService;
	private final MemberService memberService;

	@PostMapping("/items")
	@Operation(summary = "장바구니 담기: 로그인 필요")
	public ResponseEntity<?> addItemToCart(@Valid @RequestBody CartRequestDTO cartRequestDTO) {
		Member member = memberService.getLoggedInMember();

		CartResponseDTO cartResponseDTO = cartService.addItemToCart(member, cartRequestDTO);
		return ResponseEntity.ok(cartResponseDTO);
	}

	@PatchMapping("/items")
	@Operation(summary = "장바구니 아이템 수량 수정: 로그인 필요")
	public ResponseEntity<?> updateCart(@Valid @RequestBody CartRequestDTO cartRequestDTO) {
		Member member = memberService.getLoggedInMember();

		CartResponseDTO cartResponseDTO = cartService.updateCart(member, cartRequestDTO);
		return ResponseEntity.ok(cartResponseDTO);
	}

	@DeleteMapping("/items/{id}")
	@Operation(summary = "장바구니의 아이템 삭제: 로그인 필요")
	public ResponseEntity<?> deleteItemFromCart(@PathVariable Long id) {
		Member member = memberService.getLoggedInMember();

		String message = cartService.deleteItemFromCart(member, id);
		return ResponseEntity.ok(Map.of("DELETE_ITEM_FROM_CART", message));
	}

	@GetMapping("/items")
	@Operation(summary = "장바구니 조회: 로그인 필요")
	public ResponseEntity<?> getCartItems() {
		Member member = memberService.getLoggedInMember();

		List<CartResponseDTO> cartItems = cartService.getCartItems(member);

		return ResponseEntity.ok(cartItems);
	}
}
