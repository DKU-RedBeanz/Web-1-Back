package com.redbeanz.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// POST /api/users 요청. role은 받지 않습니다. (일반 회원가입으로 권한 지정 불가)
public record SignupRequest(
		@NotBlank(message = "아이디를 입력하세요.")
		@Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영문 소문자·숫자 4~20자로 입력하세요.")
		String loginId,

		@NotBlank(message = "이메일을 입력하세요.")
		// 기본 @Email은 "a@b"처럼 도메인 끝(.kr 등)이 없어도 통과하므로 프론트와 같은 규칙을 추가합니다.
		@Email(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "올바른 이메일 형식이 아닙니다.")
		@Size(max = 100, message = "이메일은 100자 이하로 입력하세요.")
		String email,

		@NotBlank(message = "비밀번호를 입력하세요.")
		@Size(min = 8, max = 64, message = "비밀번호는 8~64자로 입력하세요.")
		String password,

		@NotBlank(message = "닉네임을 입력하세요.")
		@Size(min = 2, max = 20, message = "닉네임은 2~20자로 입력하세요.")
		String nickname
) {
}
