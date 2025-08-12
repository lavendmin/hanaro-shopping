package com.hanaro.item.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanaro.item.dto.ItemDTO;
import com.hanaro.item.dto.ItemRequestDTO;
import com.hanaro.item.dto.ItemResponseDTO;
import com.hanaro.item.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/items")
@Tag(name = "Admin - Item", description = "관리자 - 상품 등록/수정/삭제")
public class ItemAdminController {
	private final ItemService itemService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "상품 등록: 관리자 권한 필요")
	public ResponseEntity<?> createItem(@Valid ItemRequestDTO itemRequestDTO) {
		ItemResponseDTO itemResponseDTO = itemService.createItem(itemRequestDTO.getFiles(), itemRequestDTO);

		return ResponseEntity.ok(itemResponseDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping("/{id}")
	@Operation(summary = "상품 수정: 관리자 권한 필요")
	public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
		ItemResponseDTO itemResponseDTO = itemService.updateItem(id, itemDTO);
		return ResponseEntity.ok(itemResponseDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	@Operation(summary = "상품 삭제: 관리자 권한 필요")
	public ResponseEntity<?> deleteItem(@PathVariable Long id) {
		String responseMsg = itemService.deleteItem(id);
		return ResponseEntity.ok(Map.of("DELETE_ITEM", responseMsg));
	}
}
