package com.redbeanz.backend.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** 개발 실습 전용: SPRING_PROFILES_ACTIVE=dev로 명시적으로 활성화합니다. */
@Configuration
@Profile("dev")
public class DevelopmentSecurityConfig {

	@Bean
	SecurityFilterChain developmentSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				// 스터디·태그 CRUD를 포함한 개발 요청은 로그인 없이 허용합니다.
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
				// Postman의 POST/PATCH/DELETE 실습에도 CSRF 토큰을 요구하지 않습니다.
				.csrf(csrf -> csrf.disable())
				// 남아 있는 Basic 인증 헤더로 인해 permitAll 요청이 401이 되는 것도 막습니다.
				.httpBasic(basic -> basic.disable())
				.formLogin(form -> form.disable())
				.logout(logout -> logout.disable())
				.build();
	}
}
