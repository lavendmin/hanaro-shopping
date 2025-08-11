package com.hanaro.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hanaro.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByEmail(String email);

	Page<Member> findByNicknameContainingIgnoreCaseOrEmailContainingIgnoreCase(String nickname, String email,
		Pageable pageable);
}
