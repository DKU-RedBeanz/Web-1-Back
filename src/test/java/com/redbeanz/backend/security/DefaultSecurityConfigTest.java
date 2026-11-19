package com.redbeanz.backend.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.redbeanz.backend.core.config.DevelopmentSecurityConfig;
import com.redbeanz.backend.core.config.UserSecurityConfig;
import com.redbeanz.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SecurityProbeController.class)
@Import({UserSecurityConfig.class, DevelopmentSecurityConfig.class})
@ActiveProfiles({"test", "security-probe"})
class DefaultSecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserRepository userRepository;

	@ParameterizedTest
	@ValueSource(strings = {"/api/practice/studies", "/api/practice/studies/1",
			"/api/practice/tags", "/api/practice/tags/1", "/api/security-probe", "/api/users/me"})
	void requestsStillRequireAuthenticationOutsideDev(String path) throws Exception {
		mockMvc.perform(get(path)).andExpect(status().isUnauthorized());
	}

	@Test
	void csrfIsStillRequiredOutsideDev() throws Exception {
		mockMvc.perform(post("/api/practice/tags").with(user("member")))
				.andExpect(status().isForbidden());
	}

	@Test
	void userListStillRequiresAdminOutsideDev() throws Exception {
		mockMvc.perform(get("/api/users").with(user("member").roles("USER")))
				.andExpect(status().isForbidden());
	}
}
