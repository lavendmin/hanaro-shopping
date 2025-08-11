package com.hanaro.member.dto;

import java.time.LocalDateTime;

import com.hanaro.member.entity.MemberRole;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MemberResponseDTO {
	private String nickname;
	private String email;
	private MemberRole role;
	private LocalDateTime createdAt;
}
