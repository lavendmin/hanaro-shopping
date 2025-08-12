package com.hanaro.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanaro.PageCond;
import com.hanaro.member.entity.Member;
import com.hanaro.member.service.MemberService;
import com.hanaro.orders.dto.OrderDTO;
import com.hanaro.orders.service.OrdersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Orders", description = "주문하기/내 주문내역 조회")
public class OrdersController {
	private final OrdersService ordersService;
	private final MemberService memberService;

	@PostMapping()
	@Operation(summary = "장바구니 기반으로 주문하기: 로그인 필요")
	public ResponseEntity<?> createOrder() {
		Member member = memberService.getLoggedInMember();
		OrderDTO orderDTOS = ordersService.createOrder(member);

		return ResponseEntity.ok(orderDTOS);
	}

	@GetMapping()
	@Operation(summary = "내 주문내역 조회: 로그인 필요")
	public ResponseEntity<?> getMyOrders(PageCond pageCond) {
		Member member = memberService.getLoggedInMember();

		Page<OrderDTO> orderDTOPage = ordersService.getMyOrders(member, pageCond);

		return ResponseEntity.ok(orderDTOPage);
	}

}
