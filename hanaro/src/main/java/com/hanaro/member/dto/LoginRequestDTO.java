package com.hanaro.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
	@NotBlank(message = "이메일을 입력해주세요.")
	@Size(min = 1, max = 50)
	@Schema(name = "email", example = "hanaro@gmail.com")
	String email,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 8)
	@Schema(name = "password", example = "12345678")
	String password
) {

}
