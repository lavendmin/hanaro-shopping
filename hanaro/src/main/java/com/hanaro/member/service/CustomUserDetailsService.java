package com.hanaro.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hanaro.member.dto.MemberDTO;
import com.hanaro.member.entity.Member;
import com.hanaro.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("*** DetailsService.loadUserByUsername = " + username);

		// 시큐리티 User에서의 username은 로그인할 때 아이디로 쓰이는 것. 즉, 우린 email
		Member member = memberRepository.findByEmail(username);

		if (member == null) {
			throw new UsernameNotFoundException(username + "is not found");
		}

		// 시큐리티의 UserDetails가 있는 User 클래스를 상속 받은 DTO/Entity를 리턴
		// 이 리턴되는 DetailsDTO를 가지고 성공(->토큰 생성) 실패(->exception) 처리 -> 핸들러
		MemberDTO memberDTO = new MemberDTO(
			member.getNickname(),
			member.getEmail(),
			member.getPassword(),
			member.getRole().name());
		System.out.println("memberDTO = " + memberDTO);
		return memberDTO;
	}
}
