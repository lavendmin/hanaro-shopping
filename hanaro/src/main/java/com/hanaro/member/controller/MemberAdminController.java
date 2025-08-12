package com.hanaro.member.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanaro.SearchCond;
import com.hanaro.member.dto.MemberResponseDTO;
import com.hanaro.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin - Member", description = "관리자 - 회원 목록/삭제")
@RequestMapping("/admin/members")
public class MemberAdminController {
	private final MemberService memberService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping()
	@Operation(summary = "회원 목록: 관리자 권한 필요")
	public ResponseEntity<?> getMembers(SearchCond searchCond) {
		Page<MemberResponseDTO> members = memberService.getMembers(searchCond);
		return ResponseEntity.ok(members);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	@Operation(summary = "회원 삭제: 관리자 권한 필요")
	public ResponseEntity<?> deleteMember(@PathVariable Long id) {
		String responseMsg = memberService.deleteMember(id);
		return ResponseEntity.ok(Map.of("DELETE_MEMBER", responseMsg));
	}
}
