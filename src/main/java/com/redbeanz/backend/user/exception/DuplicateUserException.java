package com.redbeanz.backend.user.exception;

import lombok.Getter;

// 아이디·이메일 중복 가입 (409). field로 프론트가 어느 입력칸 아래에 안내할지 구분합니다.
@Getter
public class DuplicateUserException extends RuntimeException {

	private final String field;

	private DuplicateUserException(String field, String message) {
		super(message);
		this.field = field;
	}

	public static DuplicateUserException loginId() {
		return new DuplicateUserException("loginId", "이미 사용 중인 아이디입니다.");
	}

	public static DuplicateUserException email() {
		return new DuplicateUserException("email", "이미 가입된 이메일입니다.");
	}
}
