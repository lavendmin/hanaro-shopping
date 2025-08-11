package com.hanaro.member.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO extends User {
	private String nickname;
	private String email;
	private String password;
	private String role; // 스프링 시큐리티는 role 이름을 String으로 씀.

	public MemberDTO(String nickname, String email, String password, String role) {
		// User 생성자: (username, password, authorities)
		// -> 로그인에 쓰일 계정을 써야 함
		super(email, password, List.of(new SimpleGrantedAuthority(role)));

		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Map<String, Object> getClaims() { // JWT Payload에 들어갈 claims
		Map<String, Object> map = new HashMap<>();
		map.put("nickname", nickname);
		map.put("email", email);
		map.put("password", password);
		map.put("role", role);

		return map;
	}
}
