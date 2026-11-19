package com.redbeanz.backend.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.redbeanz.backend.user.dto.SignupRequest;
import com.redbeanz.backend.user.dto.UserResponse;
import com.redbeanz.backend.user.entity.User;
import com.redbeanz.backend.user.exception.DuplicateUserException;
import com.redbeanz.backend.user.repository.UserRepository;
import com.redbeanz.backend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

// 개인 로컬 MySQL(.env)의 users 테이블로 확인합니다. 각 테스트는 롤백되어 데이터가 남지 않습니다.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserSignupIntegrationTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void 본인만_정보를_조회하고_수정하고_탈퇴할_수_있다() throws Exception {
		String loginId = "profile01";
		String password = "password123!";
		String newPassword = "changed123!";
		UserResponse user = userService.signup(new SignupRequest(loginId, "profile01@example.com", password, "테스트"));
		UserResponse other = userService.signup(new SignupRequest("profile02", "profile02@example.com", password, "다른회원"));

		mockMvc.perform(get("/api/users/me")).andExpect(status().isUnauthorized());
		var result = mockMvc.perform(get("/api/users/me").with(httpBasic(loginId, password)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.id()))
				.andExpect(jsonPath("$.password").doesNotExist())
				.andReturn();
		var session = (MockHttpSession) result.getRequest().getSession(false);
		String csrf = result.getResponse().getHeader("X-CSRF-TOKEN");
		assertThat(csrf).isNotBlank();

		mockMvc.perform(patch("/api/users/me").with(httpBasic(loginId, password))
						.contentType(MediaType.APPLICATION_JSON).content("{\"nickname\":\"새닉네임\"}"))
				.andExpect(status().isForbidden());
		mockMvc.perform(patch("/api/users/me").with(httpBasic(loginId, password))
						.session(session).header("X-CSRF-TOKEN", csrf)
						.contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"profile02@example.com\"}"))
				.andExpect(status().isConflict());
		mockMvc.perform(patch("/api/users/me").with(httpBasic(loginId, password))
						.session(session).header("X-CSRF-TOKEN", csrf)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"nickname\":\"새닉네임\",\"password\":\"changed123!\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nickname").value("새닉네임"))
				.andExpect(jsonPath("$.email").value(user.email()));
		mockMvc.perform(get("/api/users/me").with(httpBasic(loginId, password))).andExpect(status().isUnauthorized());

		mockMvc.perform(delete("/api/users/me").with(httpBasic(loginId, newPassword))
						.session(session).header("X-CSRF-TOKEN", csrf)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"id\":%d,\"password\":\"changed123!\"}".formatted(other.id())))
				.andExpect(status().isForbidden());
		mockMvc.perform(delete("/api/users/me").with(httpBasic(loginId, newPassword))
						.session(session).header("X-CSRF-TOKEN", csrf)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"id\":%d,\"password\":\"wrong-password\"}".formatted(user.id())))
				.andExpect(status().isForbidden());
		mockMvc.perform(delete("/api/users/me").with(httpBasic(loginId, newPassword))
						.session(session).header("X-CSRF-TOKEN", csrf)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"id\":%d,\"password\":\"changed123!\"}".formatted(user.id())))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/api/users/me").with(httpBasic(loginId, newPassword))).andExpect(status().isUnauthorized());
		assertThat(userRepository.existsByLoginId(other.loginId())).isTrue();
	}

	@Test
	void 관리자만_회원_목록을_조회할_수_있다() throws Exception {
		String password = "password123!";
		userService.signup(new SignupRequest("listuser01", "listuser01@example.com", password, "일반회원"));
		jdbcTemplate.update("INSERT INTO users (login_id, email, password, nickname, role) VALUES (?, ?, ?, ?, ?)",
				"listadmin01", "listadmin01@example.com", passwordEncoder.encode(password), "관리자", "ADMIN");

		mockMvc.perform(get("/api/users")).andExpect(status().isUnauthorized());
		mockMvc.perform(get("/api/users").with(httpBasic("listuser01", password))).andExpect(status().isForbidden());
		mockMvc.perform(get("/api/users").with(httpBasic("listadmin01", password)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[?(@.loginId == 'listuser01')]").isNotEmpty())
				.andExpect(jsonPath("$[*].password").isEmpty());
	}

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
