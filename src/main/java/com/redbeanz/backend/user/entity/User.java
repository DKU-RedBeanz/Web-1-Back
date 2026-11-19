package com.redbeanz.backend.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// db/01_create_users.sql의 users 테이블과 1:1로 맞춥니다. (ddl-auto: none)
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "login_id", nullable = false, unique = true, length = 20)
	private String loginId;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	// BCrypt 해시만 저장합니다. 원문 비밀번호는 보관하지 않습니다.
	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Role role;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private User(String loginId, String email, String encodedPassword, String nickname) {
		this.loginId = loginId;
		this.email = email;
		this.password = encodedPassword;
		this.nickname = nickname;
		this.role = Role.USER;
		this.createdAt = LocalDateTime.now();
	}

	// 일반 회원가입은 항상 USER 권한으로 생성합니다.
	public static User create(String loginId, String email, String encodedPassword, String nickname) {
		return new User(loginId, email, encodedPassword, nickname);
	}
}
