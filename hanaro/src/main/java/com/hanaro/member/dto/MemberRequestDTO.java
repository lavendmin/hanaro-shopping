package com.hanaro.member.dto;

import com.hanaro.member.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberRequestDTO {
	@Schema(name = "nickname", example = "newMember")
	private String nickname;

	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	@Schema(name = "email", example = "newMember@gmail.com")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Schema(name = "password", example = "12345678")
	private String password;

	public Member toEntity() {
		Member member = new Member();
		member.setNickname(nickname);
		member.setEmail(email);
		member.setPassword(password);

		return member;
	}
}
