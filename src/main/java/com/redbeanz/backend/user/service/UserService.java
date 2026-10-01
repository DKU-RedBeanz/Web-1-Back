package com.redbeanz.backend.user.service;

import com.redbeanz.backend.user.dto.SignupRequest;
import com.redbeanz.backend.user.dto.UserResponse;
import com.redbeanz.backend.user.entity.User;
import com.redbeanz.backend.user.exception.DuplicateUserException;
import com.redbeanz.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserResponse signup(SignupRequest request) {
		if (userRepository.existsByLoginId(request.loginId())) {
			throw DuplicateUserException.loginId();
		}
		if (userRepository.existsByEmail(request.email())) {
			throw DuplicateUserException.email();
		}

		User user = User.create(
				request.loginId(),
				request.email(),
				passwordEncoder.encode(request.password()),
				request.nickname()
		);

		try {
			return UserResponse.from(userRepository.saveAndFlush(user));
		} catch (DataIntegrityViolationException exception) {
			// 동시에 같은 값으로 가입한 경우 DB UNIQUE 제약이 마지막으로 막습니다.
			String cause = String.valueOf(exception.getMostSpecificCause().getMessage());
			if (cause.contains("uk_users_login_id")) {
				throw DuplicateUserException.loginId();
			}
			if (cause.contains("uk_users_email")) {
				throw DuplicateUserException.email();
			}
			throw exception;
		}
	}
}
