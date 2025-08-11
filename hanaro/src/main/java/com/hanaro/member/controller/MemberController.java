package com.hanaro.member.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanaro.SearchCond;
import com.hanaro.member.dto.LoginRequestDTO;
import com.hanaro.member.dto.MemberRequestDTO;
import com.hanaro.member.dto.MemberResponseDTO;
import com.hanaro.member.service.MemberService;
import com.hanaro.security.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	private final MemberService memberService;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/login")
	@Tag(name = "로그인", description = "사용자 로그인")
	public ResponseEntity<?> login(LoginRequestDTO loginRequestDTO) {
		try {
			Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					loginRequestDTO.email(), loginRequestDTO.password()
				)
			);
			return ResponseEntity.ok(JwtUtil.authenticationToClaims(authenticate));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid!");
		}

	}

	// 회원 가입
	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@Valid @RequestBody MemberRequestDTO memberRequestDTO) {
		MemberResponseDTO memberResponseDTO = memberService.createMember(memberRequestDTO);
		return ResponseEntity.ok(memberResponseDTO);
	}

	// 회원 목록
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping()
	public ResponseEntity<?> getMembers(SearchCond searchCond) {
		Page<MemberResponseDTO> members = memberService.getMembers(searchCond);
		return ResponseEntity.ok(members);
	}

	// 회원 삭제
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable Long id) {
		String responseMsg = memberService.deleteMember(id);
		return ResponseEntity.ok(Map.of("delete member", responseMsg));
	}
}
