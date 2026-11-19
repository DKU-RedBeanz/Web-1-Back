package com.redbeanz.backend.user;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.redbeanz.backend.user.controller.UserController;
import com.redbeanz.backend.user.dto.SignupRequest;
import com.redbeanz.backend.user.dto.UserResponse;
import com.redbeanz.backend.user.entity.Role;
import com.redbeanz.backend.user.exception.DuplicateUserException;
import com.redbeanz.backend.user.service.UserService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// 컨트롤러의 요청 검증·응답 형식만 확인합니다.
// 공개 경로 허용은 Security #3의 SecurityConfig에서 설정하므로 여기서는 보안 필터를 끕니다.
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

	private static final String VALID_BODY = """
			{ "loginId": "redbeanz", "email": "study@redbeanz.kr", "password": "password123!", "nickname": "팥빙수" }
			""";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@Test
	void 정상_가입은_201과_비밀번호_없는_회원_정보() throws Exception {
		given(userService.signup(any(SignupRequest.class))).willReturn(new UserResponse(
				1L, "redbeanz", "study@redbeanz.kr", "팥빙수", Role.USER, LocalDateTime.of(2026, 10, 1, 12, 0)));

		mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.loginId").value("redbeanz"))
				.andExpect(jsonPath("$.role").value("USER"))
				.andExpect(jsonPath("$", not(hasKey("password"))));
	}

	@Test
	void 입력_오류는_400과_필드별_메시지() throws Exception {
		String body = """
				{ "loginId": "AB", "email": "study@redbeanz", "password": "short", "nickname": "" }
				""";

		mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.loginId").value("아이디는 영문 소문자·숫자 4~20자로 입력하세요."))
				.andExpect(jsonPath("$.errors.email").value("올바른 이메일 형식이 아닙니다."))
				.andExpect(jsonPath("$.errors.password").value("비밀번호는 8~64자로 입력하세요."))
				.andExpect(jsonPath("$.errors.nickname").exists());
	}

	@Test
	void 요청에_role을_넣어도_무시된다() throws Exception {
		given(userService.signup(any(SignupRequest.class))).willReturn(new UserResponse(
				1L, "redbeanz", "study@redbeanz.kr", "팥빙수", Role.USER, LocalDateTime.of(2026, 10, 1, 12, 0)));
		String body = """
				{ "loginId": "redbeanz", "email": "study@redbeanz.kr", "password": "password123!", "nickname": "팥빙수", "role": "ADMIN" }
				""";

		mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.role").value("USER"));
	}

	@Test
	void 아이디_중복은_409() throws Exception {
		given(userService.signup(any(SignupRequest.class))).willThrow(DuplicateUserException.loginId());

		mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.errors.loginId").value("이미 사용 중인 아이디입니다."));
	}

	@Test
	void 이메일_중복은_409() throws Exception {
		given(userService.signup(any(SignupRequest.class))).willThrow(DuplicateUserException.email());

		mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.errors.email").value("이미 가입된 이메일입니다."));
	}
}
