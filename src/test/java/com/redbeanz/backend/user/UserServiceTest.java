package com.redbeanz.backend.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.redbeanz.backend.user.dto.SignupRequest;
import com.redbeanz.backend.user.dto.UserResponse;
import com.redbeanz.backend.user.entity.Role;
import com.redbeanz.backend.user.entity.User;
import com.redbeanz.backend.user.exception.DuplicateUserException;
import com.redbeanz.backend.user.repository.UserRepository;
import com.redbeanz.backend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	private static final SignupRequest REQUEST =
			new SignupRequest("redbeanz", "study@redbeanz.kr", "password123!", "팥빙수");

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@Test
	void 회원가입하면_비밀번호를_암호화해_USER_권한으로_저장한다() {
		given(passwordEncoder.encode("password123!")).willReturn("encoded-hash");
		given(userRepository.saveAndFlush(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

		UserResponse response = userService.signup(REQUEST);

		ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
		verify(userRepository).saveAndFlush(saved.capture());
		assertThat(saved.getValue().getPassword()).isEqualTo("encoded-hash");
		assertThat(saved.getValue().getRole()).isEqualTo(Role.USER);
		assertThat(response.loginId()).isEqualTo("redbeanz");
		assertThat(response.role()).isEqualTo(Role.USER);
	}

	@Test
	void 아이디가_중복이면_저장하지_않고_예외() {
		given(userRepository.existsByLoginId("redbeanz")).willReturn(true);

		assertThatThrownBy(() -> userService.signup(REQUEST))
				.isInstanceOf(DuplicateUserException.class)
				.extracting("field").isEqualTo("loginId");
		verify(userRepository, never()).saveAndFlush(any());
	}

	@Test
	void 이메일이_중복이면_저장하지_않고_예외() {
		given(userRepository.existsByEmail("study@redbeanz.kr")).willReturn(true);

		assertThatThrownBy(() -> userService.signup(REQUEST))
				.isInstanceOf(DuplicateUserException.class)
				.extracting("field").isEqualTo("email");
		verify(userRepository, never()).saveAndFlush(any());
	}
}
