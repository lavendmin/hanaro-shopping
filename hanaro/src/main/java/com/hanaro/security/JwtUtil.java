package com.hanaro.security;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;

import com.hanaro.member.dto.MemberDTO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

public class JwtUtil { // util 클래스는 여기저기서 쓰기 때문에 public static으로
	private static final SecretKey KEY = Keys.hmacShaKeyFor(
		"QFPg8v69EpZxgKNbGThRGoHjYxXsXdSddHFLWCPnXdY=".getBytes(StandardCharsets.UTF_8));

	// @Value("${spring.jwt.secret}")
	// private String secret;
	// private static final SecretKey KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

	// private static final SecretKey SECRET_KEY;
	//
	// public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
	// 	this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	// }

	public static String generateToken(Map<String, Object> valueMap, int min) {
		// 암호화 알고리즘 등록
		// SecretKey key = Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8));

		String jwtStr = Jwts.builder().setHeader(Map.of("typ", "JWT"))
			.setClaims(valueMap) // payload에 전달 받은 claims 추가
			.setIssuedAt(Date.from(ZonedDateTime.now().toInstant())) // 발행시간
			.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // 만료시간
			.signWith(KEY).compact();
		System.out.println("jwtStr = " + jwtStr);
		return jwtStr;
	}

	public static Map<String, Object> validateToken(String token) {
		Map<String, Object> claim = null;
		// SecretKey key = null;

		try {
			// key = Keys.hmacShaKeyFor(JwtUtil.KEY.getBytes(StandardCharsets.UTF_8)); // 복화해 해야 하기 때문에 key를 가져옴
			claim = Jwts.parserBuilder()
				.setSigningKey(KEY)
				.build()
				.parseClaimsJws(token)
				.getBody(); // 키 가져와서 parsing 즉, 복호화
		} catch (WeakKeyException e) {
			throw new CustomJwtException("WeakKeyException");
		} catch (MalformedJwtException e) {
			throw new CustomJwtException("MalformedJwtException");
		} catch (ExpiredJwtException e) {
			throw new CustomJwtException("ExpiredJwtException");
		} catch (InvalidClaimException e) {
			throw new CustomJwtException("InvalidClaimException");
		} catch (JwtException e) {
			throw new CustomJwtException("JwtException");
		} catch (Exception e) {
			throw new CustomJwtException("UnknownException");
		}

		return claim;
	}

	public static Map<String, Object> authenticationToClaims(Authentication authentication) {
		MemberDTO dto = (MemberDTO)authentication.getPrincipal();
		MemberDTO memberDTO = new MemberDTO(dto.getNickname(), dto.getEmail(), "", dto.getRole());

		Map<String, Object> claims = memberDTO.getClaims();

		claims.put("accessToken", JwtUtil.generateToken(claims, 10));
		claims.put("refreshToken", JwtUtil.generateToken(claims, 600));
		return claims;
	}
}
