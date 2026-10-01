package com.redbeanz.backend.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.redbeanz.backend.user.dto.SignupRequest;
import com.redbeanz.backend.user.dto.UserResponse;
import com.redbeanz.backend.user.entity.User;
import com.redbeanz.backend.user.exception.DuplicateUserException;
import com.redbeanz.backend.user.repository.UserRepository;
import com.redbeanz.backend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

// 개인 로컬 MySQL(.env)의 users 테이블로 확인합니다. 각 테스트는 롤백되어 데이터가 남지 않습니다.
@SpringBootTest
@Transactional
class UserSignupIntegrationTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void 저장한_비밀번호_해시를_PasswordEncoder로_검증할_수_있다() {
		UserResponse response = userService.signup(
				new SignupRequest("itest01", "itest01@redbeanz.kr", "password123!", "테스트"));

		User saved = userRepository.findByLoginId("itest01").orElseThrow();
		assertThat(response.id()).isEqualTo(saved.getId());
		assertThat(saved.getPassword()).isNotEqualTo("password123!").startsWith("$2");
		assertThat(passwordEncoder.matches("password123!", saved.getPassword())).isTrue();
		assertThat(passwordEncoder.matches("wrong-password", saved.getPassword())).isFalse();
	}

	@Test
	void 같은_아이디나_이메일로_다시_가입하면_409_예외() {
		userService.signup(new SignupRequest("itest02", "itest02@redbeanz.kr", "password123!", "테스트"));

		assertThatThrownBy(() -> userService.signup(
				new SignupRequest("itest02", "other@redbeanz.kr", "password123!", "테스트")))
				.isInstanceOf(DuplicateUserException.class).extracting("field").isEqualTo("loginId");
		assertThatThrownBy(() -> userService.signup(
				new SignupRequest("itest03", "itest02@redbeanz.kr", "password123!", "테스트")))
				.isInstanceOf(DuplicateUserException.class).extracting("field").isEqualTo("email");
	}

	@Test
	void 같은_닉네임은_허용된다() {
		userService.signup(new SignupRequest("itest04", "itest04@redbeanz.kr", "password123!", "같은닉"));
		userService.signup(new SignupRequest("itest05", "itest05@redbeanz.kr", "password123!", "같은닉"));

		assertThat(userRepository.existsByLoginId("itest05")).isTrue();
	}

	@Test
	void 서비스_검증을_거치지_않아도_DB_UNIQUE_제약이_중복을_막는다() {
		userRepository.saveAndFlush(User.create("itest06", "itest06@redbeanz.kr", "hash", "테스트"));

		assertThatThrownBy(() -> userRepository.saveAndFlush(
				User.create("itest06", "another@redbeanz.kr", "hash", "테스트")))
				.isInstanceOf(DataIntegrityViolationException.class);
	}
}
