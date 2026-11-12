package com.redbeanz.backend.user.controller;

import com.redbeanz.backend.user.dto.DeleteUserRequest;
import com.redbeanz.backend.user.dto.SignupRequest;
import com.redbeanz.backend.user.dto.UpdateUserRequest;
import com.redbeanz.backend.user.dto.UserResponse;
import com.redbeanz.backend.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/login-id-availability")
	public Map<String, Boolean> loginIdAvailability(
			@RequestParam @Pattern(regexp = SignupRequest.LOGIN_ID_PATTERN) String loginId) {
		return Map.of("available", userService.isLoginIdAvailable(loginId));
	}

	// 회원가입: 201 / 400(입력 오류) / 409(아이디·이메일 중복)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse signup(@Valid @RequestBody SignupRequest request) {
		return userService.signup(request);
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponse> me(Principal principal, CsrfToken csrf) {
		return ResponseEntity.ok()
				.header(csrf.getHeaderName(), csrf.getToken())
				.body(userService.me(principal.getName()));
	}

	@GetMapping
	public List<UserResponse> list() {
		return userService.list();
	}

	@PatchMapping("/me")
	public UserResponse update(Principal principal, @Valid @RequestBody UpdateUserRequest request) {
		return userService.update(principal.getName(), request);
	}

	@DeleteMapping("/me")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(Principal principal, @Valid @RequestBody DeleteUserRequest request) {
		userService.delete(principal.getName(), request);
	}
}
