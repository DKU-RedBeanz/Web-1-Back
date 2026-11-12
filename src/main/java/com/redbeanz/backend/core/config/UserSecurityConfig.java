package com.redbeanz.backend.core.config;

import com.redbeanz.backend.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class UserSecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService(UserRepository users) {
		return loginId -> users.findByLoginId(loginId)
				.map(user -> User.withUsername(user.getLoginId())
						.password(user.getPassword())
						.roles(user.getRole().name())
						.build())
				.orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		var paths = PathPatternRequestMatcher.withDefaults();
		var signup = paths.matcher(HttpMethod.POST, "/api/users");
		var availability = paths.matcher(HttpMethod.GET, "/api/users/login-id-availability");

		return http
				.csrf(csrf -> csrf.ignoringRequestMatchers(signup))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(signup, availability).permitAll()
						.requestMatchers(paths.matcher(HttpMethod.GET, "/api/users")).hasRole("ADMIN")
						.anyRequest().authenticated())
				.httpBasic(withDefaults())
				.build();
	}
}
