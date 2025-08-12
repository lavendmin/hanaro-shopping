package com.hanaro.member;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hanaro.member.entity.Member;
import com.hanaro.member.entity.MemberRole;
import com.hanaro.member.repository.MemberRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberRepositoryTest {
	@Autowired
	MemberRepository memberRepository;

	// 암호화 bean이 스프링에 있어서 @DataJpaTest 못 쓰고 @SpringBootTest 써야 함.
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@Order(1)
	void saveTest() {
		// admin 생성
		Member member = Member.builder()
			.nickname("hanaro")
			.email("hanaro@gmail.com")
			.password(passwordEncoder.encode("12345678"))
			.role(MemberRole.ROLE_ADMIN)
			.build();

		// user 생성 -> 디폴트로 ROLE_USER
		Member mbr = new Member();
		mbr.setNickname("Lee");
		mbr.setEmail("lee@gmail.com");
		mbr.setPassword(passwordEncoder.encode("12345678"));

		// 저장
		Member savedMember = memberRepository.save(member);
		Member savedMbr = memberRepository.save(mbr);

		// 레포지토리에서 불러오기
		Member foundMember = memberRepository.findById(savedMember.getId()).orElseThrow();
		Member foundMbr = memberRepository.findById(savedMbr.getId()).orElseThrow();

		System.out.println("savedMember: " + savedMember);
		System.out.println("foundMember: " + foundMember);

		/*
		시큐리티 도입 전에는 테스트 성공했는데 시큐리티 도입 후에는 실패한 이유
		@DataJpaTest (기본 @Transactional) 사용했어서
		하나의 persistent context(같은 트랜잭션) 안에서 돌면서 findById가 동일 인스턴스 반환했던 것.
		=> @SpringBootTest 쓸 때엔, 엔티티에 @EqualsAndHashCode 추가

		+) 위를 테스트해보기 위해서 @DataJpaTest로 바꾸기만 했을 때 실패한 이유
		@DataJpaTest는 실제DB 쓰지 않고 내장DB(H2, HSQL, Derby)로 데이터소스 교체해서
		@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)가 필요함
		 */

		// 검증
		assertEquals(savedMember, foundMember);
		assertEquals(savedMbr, foundMbr);
	}

	@Test
	@Order(2)
	void addTest() {
		long preCount = memberRepository.count();

		// 일반 유저 10명 추가
		List<Member> members = Stream.iterate(1, n -> n + 1).limit(20)
			.map(n -> Member.builder()
				.nickname("user" + n)
				.email("user" + n + "@gmail.com")
				.password(passwordEncoder.encode("12345678"))
				.build()
			).toList();

		memberRepository.saveAll(members);

		assertEquals(preCount + 20, memberRepository.findAll().size());
	}
}
