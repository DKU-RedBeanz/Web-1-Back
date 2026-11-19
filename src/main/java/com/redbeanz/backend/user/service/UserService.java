package com.redbeanz.backend.user.service;

import com.redbeanz.backend.user.dto.DeleteUserRequest;
import com.redbeanz.backend.user.dto.SignupRequest;
import com.redbeanz.backend.user.dto.UpdateUserRequest;
import com.redbeanz.backend.user.dto.UserResponse;
import com.redbeanz.backend.user.entity.User;
import com.redbeanz.backend.user.exception.DuplicateUserException;
import com.redbeanz.backend.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

	private static final int MAX_PASSWORD_BYTES = 72;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public boolean isLoginIdAvailable(String loginId) {
		return !userRepository.existsByLoginId(loginId);
	}

	@Transactional
	public UserResponse signup(SignupRequest request) {
		if (!isLoginIdAvailable(request.loginId())) {
			throw DuplicateUserException.loginId();
		}
		if (userRepository.existsByEmail(request.email())) {
			throw DuplicateUserException.email();
		}

		return save(User.create(
				request.loginId(),
				request.email(),
				encodePassword(request.password()),
				request.nickname()
		));
	}

	@Transactional(readOnly = true)
	public UserResponse me(String loginId) {
		return userRepository.findByLoginId(loginId)
				.map(UserResponse::from)
				.orElseThrow(UserService::userNotFound);
	}

	@Transactional(readOnly = true)
	public List<UserResponse> list() {
		List<UserResponse> users = new ArrayList<>();
		for (User user : userRepository.findAll(Sort.by("id"))) {
			users.add(UserResponse.from(user));
		}
		return users;
	}

	@Transactional
	public UserResponse update(String loginId, UpdateUserRequest request) {
		if (request.email() == null && request.nickname() == null && request.password() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "변경할 정보를 입력하세요.");
		}

		User user = userRepository.findForUpdateByLoginId(loginId).orElseThrow(UserService::userNotFound);
		if (request.email() != null && userRepository.existsByEmailAndIdNot(request.email(), user.getId())) {
			throw DuplicateUserException.email();
		}
		String encodedPassword = request.password() == null ? null : encodePassword(request.password());
		user.update(request.email(), request.nickname(), encodedPassword);
		return save(user);
	}

	@Transactional
	public void delete(String loginId, DeleteUserRequest request) {
		User user = userRepository.findForUpdateByLoginId(loginId).orElseThrow(UserService::userNotFound);
		if (!user.getId().equals(request.id())
				|| request.password().getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES
				|| !passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		userRepository.delete(user);
	}

	private UserResponse save(User user) {
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

	private String encodePassword(String password) {
		if (password.isBlank() || password.getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 공백만으로 구성할 수 없으며 UTF-8로 72바이트 이하여야 합니다.");
		}
		return passwordEncoder.encode(password);
	}

	private static ResponseStatusException userNotFound() {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다.");
	}
}
