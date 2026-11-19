package com.redbeanz.backend.user.dto;

import com.redbeanz.backend.user.entity.Role;
import com.redbeanz.backend.user.entity.User;
import java.time.LocalDateTime;

// 회원 응답. 비밀번호·해시는 포함하지 않습니다.
public record UserResponse(
		Long id,
		String loginId,
		String email,
		String nickname,
		Role role,
		LocalDateTime createdAt
) {

	public static UserResponse from(User user) {
		return new UserResponse(
				user.getId(),
				user.getLoginId(),
				user.getEmail(),
				user.getNickname(),
				user.getRole(),
				user.getCreatedAt()
		);
	}
}
