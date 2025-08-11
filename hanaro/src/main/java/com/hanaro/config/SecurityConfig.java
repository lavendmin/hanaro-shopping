package com.hanaro.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hanaro.security.CustomAccessDeniedHandler;
import com.hanaro.security.JwtAuthenticationFilter;
import com.hanaro.security.LoginFailureHandler;
import com.hanaro.security.LoginSuccessHandler;

import lombok.extern.log4j.Log4j2;

@Configuration // 스프링이 설정임을 알 수 있도록. Bean 등록할 수 있도록
@Log4j2
@EnableMethodSecurity
public class SecurityConfig {
	/*
	SecurityFilterChain
	클라이언트(브라우저) 요청 후 단계 & Dispatch Servlet 전 단계
	로그인 처리: 인증(Authentication), 인가(Authorization)
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("--- SecurityConfig filterChain");
		System.out.println("** SecurityConfig filterChain");

		http
			// .httpBasic(AbstractHttpConfigurer::disable) // HTML 안 쓴다는 의미
			.csrf(AbstractHttpConfigurer::disable) // 개발할 때 사이트 간 요청 조작하는 걸 꺼둠. JWT는 조작 불가능.
			.cors(config -> config.configurationSource(corsConfigurationSource()))
			.sessionManagement(config ->
				config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(form -> form
				.loginPage("/api/members/login")
				// .loginProcessingUrl("/api/members/login") // 생략되면 위의 loginPage 따라감
				.successHandler(new LoginSuccessHandler()) // loginPage에서 성공하면 LoginSuccessHandler 실행
				.failureHandler(new LoginFailureHandler()) // 실패하면 LoginFailureHandler 실행
			)
			.exceptionHandling(config
				-> config.accessDeniedHandler(new CustomAccessDeniedHandler())) // 권한 핸들러 (403에러)
			.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		// Stateless: 세션을 만들지도 기존 것을 쓰지도 않음. JWT 같은 세션 사용하지 않는 토큰 방식 쓸 때 사용

		return http.build();
	}

	// 패스워드 암호화
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager
		(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/*
	CORS(Cross-Origin Resource Sharing) 설정 정의
	서로 다른 도메인 간 이미지, api와 같은 자원을 공유할 수 있도록
	 */
	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// Read Only -> Arrays.asList()와 달리 immutable한 List.of() -> 조작 X
		config.setAllowedOriginPatterns(List.of("*")); // 출발지 도메인 허용
		config.setAllowedMethods(List.of(
			HttpMethod.GET.name(), // enum타입 String으로 받아오기 위해서 .name()
			HttpMethod.POST.name(),
			HttpMethod.PATCH.name(),
			HttpMethod.DELETE.name(),
			HttpMethod.OPTIONS.name() // 허용 요청 메소드 확인
		));
		config.setAllowedHeaders(List.of( // 헤더에 들어갈 것
			HttpHeaders.AUTHORIZATION,
			HttpHeaders.CACHE_CONTROL,
			HttpHeaders.CONTENT_TYPE));
		config.setAllowCredentials(true); // 로그인 지속되도록

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config); // 위의 configuration을 모든 곳에 대해서 등록
		return source;
	}
}
