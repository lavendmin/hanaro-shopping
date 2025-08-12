package com.hanaro.item.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanaro.SearchCond;
import com.hanaro.item.dto.ItemDTO;
import com.hanaro.item.dto.ItemRequestDTO;
import com.hanaro.item.dto.ItemResponseDTO;
import com.hanaro.item.service.ItemService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Tag(name = "Item", description = "상품 등록/조회/수정/삭제")
public class ItemController {
	private final ItemService itemService;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@Tag(name = "상품 등록", description = "관리자 권한 필요")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createItem(ItemRequestDTO itemRequestDTO) {
		ItemResponseDTO itemResponseDTO = itemService.createItem(itemRequestDTO.getFiles(), itemRequestDTO);

		return ResponseEntity.ok(itemResponseDTO);
	}

	@Tag(name = "상품 목록 조회")
	@GetMapping()
	public ResponseEntity<?> getItems(SearchCond searchCond) {
		Page<ItemResponseDTO> items = itemService.getItems(searchCond);
		return ResponseEntity.ok(items);
	}

	// TODO 상품 상세 조회
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@Tag(name = "상품 수정", description = "관리자 권한 필요")
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
		ItemResponseDTO itemResponseDTO = itemService.updateItem(id, itemDTO);
		return ResponseEntity.ok(itemResponseDTO);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@Tag(name = "상품 삭제", description = "관리자 권한 필요")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteItem(@PathVariable Long id) {
		String responseMsg = itemService.deleteItem(id);
		return ResponseEntity.ok(Map.of("delete item", responseMsg));
	}

}
