package com.redbeanz.backend.user.controller;

import com.redbeanz.backend.user.dto.SignupRequest;
import com.redbeanz.backend.user.dto.UserResponse;
import com.redbeanz.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 회원가입: 201 / 400(입력 오류) / 409(아이디·이메일 중복)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse signup(@Valid @RequestBody SignupRequest request) {
		return userService.signup(request);
	}
}
