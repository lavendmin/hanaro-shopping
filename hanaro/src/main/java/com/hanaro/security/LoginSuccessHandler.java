package com.hanaro.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaro.member.dto.MemberDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		System.out.println("*** SuccessHandler = " + authentication);

		MemberDTO dto = (MemberDTO)authentication.getPrincipal(); // UserDetailsService의 서비스에서 리턴하는 값

		// 비밀번호 빈 문자열로
		MemberDTO memberDTO = new MemberDTO(dto.getNickname(), dto.getEmail(), "", dto.getRole());

		// claim을 access 토큰에 담아서 클라이언트에게 전송
		Map<String, Object> claims = memberDTO.getClaims(); // 빈 문자열의 패스워드가 있는 토큰 구워짐
		claims.put("accessToken", JwtUtil.generateToken(claims, 10)); // 10분 이내에 서버에 요청해야 access token 발급
		claims.put("refreshToken",
			JwtUtil.generateToken(claims, 60 * 24)); // 24시간 동안 refresh token으로 access token 재발급 가능

		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(objectMapper.writeValueAsString(claims));
		out.close();
	}
}
