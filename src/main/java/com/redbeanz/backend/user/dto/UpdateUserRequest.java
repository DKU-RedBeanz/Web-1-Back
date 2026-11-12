package com.redbeanz.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
		@Email(regexp = SignupRequest.EMAIL_PATTERN, message = "올바른 이메일 형식이 아닙니다.")
		@Size(min = 1, max = 100, message = "이메일은 100자 이하로 입력하세요.")
		String email,

		@Pattern(regexp = "(?s).*\\S.*", message = "닉네임을 입력하세요.")
		@Size(min = 2, max = 20, message = "닉네임은 2~20자로 입력하세요.")
		String nickname,

		@Size(min = 8, max = 64, message = "비밀번호는 8~64자로 입력하세요.")
		String password
) {
}
