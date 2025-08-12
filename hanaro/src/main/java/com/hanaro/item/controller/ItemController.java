package com.hanaro.item.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanaro.SearchCond;
import com.hanaro.item.dto.ItemDTO;
import com.hanaro.item.dto.ItemResponseDTO;
import com.hanaro.item.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Tag(name = "Item", description = "상품 목록/상세 조회")
public class ItemController {
	private final ItemService itemService;

	@GetMapping()
	@Operation(summary = "상품 목록 조회")
	public ResponseEntity<?> getItems(SearchCond searchCond) {
		Page<ItemDTO> items = itemService.getItems(searchCond);
		return ResponseEntity.ok(items);
	}

	@GetMapping("/{id}")
	@Operation(summary = "상품 상세 조회")
	public ResponseEntity<?> getItemById(@PathVariable Long id) {
		ItemResponseDTO item = itemService.getItemById(id);
		return ResponseEntity.ok(item);
	}

}
