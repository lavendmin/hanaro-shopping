package com.hanaro.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanaro.PageCond;
import com.hanaro.SearchOrdersCond;
import com.hanaro.orders.dto.OrderDTO;
import com.hanaro.orders.dto.SaleItemStatDTO;
import com.hanaro.orders.dto.SaleStatDTO;
import com.hanaro.orders.service.OrdersService;
import com.hanaro.orders.service.SaleStatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin - Orders", description = "주문하기/내 주문내역 조회/전체 주문내역 조회")
@RequestMapping("/admin/orders")
public class OrdersAdminController {
	private final OrdersService ordersService;
	private final SaleStatService saleStatService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping()
	@Operation(summary = "전체 주문 내역 조회: 관리자 권한 필요",
		description = "회원 닉네임과 이메일, 주문 내역 상품명과 설명을 검색하고 날짜 필터링을 할 수 있습니다.")
	public ResponseEntity<?> getOrders(SearchOrdersCond searchOrdersCond) {
		Page<OrderDTO> orderDTOPage = ordersService.getOrders(searchOrdersCond);

		return ResponseEntity.ok(orderDTOPage);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/saleStat")
	@Operation(summary = "일별 총 매출액 통계 조회")
	public ResponseEntity<?> getSaleStat(@Parameter(description = "페이지 조건",
		schema = @Schema(implementation = PageCond.class, defaultValue = "{\"sortField\":\"saledt\"}")) PageCond pageCond) {
		pageCond.setSortField("saledt");
		Page<SaleStatDTO> saleStats = saleStatService.getSaleStat(pageCond);
		return ResponseEntity.ok(saleStats);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/saleItemStat")
	@Operation(summary = "일별 상품별 매출액 통계 조회")
	public ResponseEntity<?> getSaleItemStat(PageCond pageCond) {
		pageCond.setSortField("saledt");
		Page<SaleItemStatDTO> saleStats = saleStatService.getSaleItemStat(pageCond);
		return ResponseEntity.ok(saleStats);
	}
}
