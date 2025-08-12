package com.hanaro.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanaro.PageCond;
import com.hanaro.SearchOrdersCond;
import com.hanaro.member.entity.Member;
import com.hanaro.member.service.MemberService;
import com.hanaro.orders.dto.OrderDTO;
import com.hanaro.orders.service.OrdersService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Orders", description = "주문하기/내 주문내역 조회/전체 주문내역 조회")
public class OrdersController {
	private final OrdersService ordersService;
	private final MemberService memberService;

	@Tag(name = "장바구니 내역 주문하기", description = "로그인 필요")
	@PostMapping()
	public ResponseEntity<?> createOrder() {
		Member member = memberService.getLoggedInMember();
		OrderDTO orderDTOS = ordersService.createOrder(member);

		return ResponseEntity.ok(orderDTOS);
	}

	@Tag(name = "내 주문내역 조회", description = "로그인 필요")
	@GetMapping("/my")
	public ResponseEntity<?> getMyOrders(PageCond pageCond) {
		// TODO 검색?
		Member member = memberService.getLoggedInMember();

		Page<OrderDTO> orderDTOPage = ordersService.getMyOrders(member, pageCond);

		return ResponseEntity.ok(orderDTOPage);
	}

	@Tag(name = "주문 내역 조회", description = "관리자 권한 필요")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping()
	public ResponseEntity<?> getOrders(SearchOrdersCond searchOrdersCond) {
		Page<OrderDTO> orderDTOPage = ordersService.getOrders(searchOrdersCond);

		return ResponseEntity.ok(orderDTOPage);
	}
}
