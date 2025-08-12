package com.hanaro.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hanaro.SearchCond;
import com.hanaro.cart.entity.Cart;
import com.hanaro.cart.repository.CartRepository;
import com.hanaro.member.dto.MemberRequestDTO;
import com.hanaro.member.dto.MemberResponseDTO;
import com.hanaro.member.entity.Member;
import com.hanaro.member.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;

	@Override
	public Page<MemberResponseDTO> getMembers(SearchCond searchCond) {
		Pageable pageable = searchCond.getPageable();

		Page<Member> members;
		if (searchCond.needSearch()) {
			String searchTerm = searchCond.getSearchTerm();
			members = memberRepository.findByNicknameContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm,
				searchTerm, pageable);
		} else {
			members = memberRepository.findAll(pageable);
		}

		return members.map(MemberServiceImpl::toDTO);
	}

	@Override
	public MemberResponseDTO createMember(MemberRequestDTO memberRequestDTO) {
		Member member = memberRequestDTO.toEntity();
		member.setPassword(passwordEncoder.encode(memberRequestDTO.getPassword()));
		memberRepository.save(member);

		// 회원가입할 때 장바구니 자동 생성
		Cart cart = new Cart();
		cart.setCustomer(member);
		cartRepository.save(cart);

		return MemberServiceImpl.toDTO(member);
	}

	@Override
	public String deleteMember(long id) {
		Member member = memberRepository.findById(id).orElseThrow(
			() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다."));

		memberRepository.delete(member);
		return "해당 회원을 삭제하였습니다.";
	}

	@Override
	public long getMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Member member = memberRepository.findByEmail(email).orElseThrow(
			() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

		return member.getId();
	}

	@Override
	public Member getLoggedInMember() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return memberRepository.findByEmail(email).orElseThrow(
			() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
	}

	public static MemberResponseDTO toDTO(Member member) {
		return MemberResponseDTO.builder()
			.nickname(member.getNickname())
			.email(member.getEmail())
			.role(member.getRole())
			.createdAt(member.getCreatedAt())
			.build();
	}

}
