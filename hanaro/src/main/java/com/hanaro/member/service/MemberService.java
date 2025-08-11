package com.hanaro.member.service;

import org.springframework.data.domain.Page;

import com.hanaro.SearchCond;
import com.hanaro.member.dto.MemberRequestDTO;
import com.hanaro.member.dto.MemberResponseDTO;
import com.hanaro.member.entity.Member;

import jakarta.validation.Valid;

public interface MemberService {
	Page<MemberResponseDTO> getMembers(SearchCond searchCond);

	MemberResponseDTO createMember(@Valid MemberRequestDTO memberRequestDTO);

	String deleteMember(long id);

	long getMemberId();

	Member getLoggedInMember();
}
