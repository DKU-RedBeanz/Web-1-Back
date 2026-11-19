package com.redbeanz.backend.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// 회원가입(Back #7)에 필요한 BCrypt 인코더입니다.
// Security #3의 SecurityConfig에 PasswordEncoder 빈이 추가되면 이 클래스를 제거하고 그 빈을 함께 사용합니다.
@Configuration
public class PasswordEncoderConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
