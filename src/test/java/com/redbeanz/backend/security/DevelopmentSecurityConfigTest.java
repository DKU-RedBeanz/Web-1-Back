package com.redbeanz.backend.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.redbeanz.backend.core.config.DevelopmentSecurityConfig;
import com.redbeanz.backend.core.config.UserSecurityConfig;
import com.redbeanz.backend.user.repository.UserRepository;
import jakarta.servlet.DispatcherType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SecurityProbeController.class)
@Import({UserSecurityConfig.class, DevelopmentSecurityConfig.class})
@ActiveProfiles({"dev", "security-probe"})
class DevelopmentSecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserRepository userRepository;

	@ParameterizedTest
	@CsvSource({
			"GET, /api/practice/studies", "GET, /api/practice/studies/1",
			"POST, /api/practice/studies", "PATCH, /api/practice/studies/1",
			"DELETE, /api/practice/studies/1",
			"GET, /api/practice/tags", "GET, /api/practice/tags/1",
			"POST, /api/practice/tags", "PATCH, /api/practice/tags/1",
			"DELETE, /api/practice/tags/1"
	})
	void practiceRequestsNeedNeitherLoginNorCsrf(String method, String path) throws Exception {
		mockMvc.perform(request(HttpMethod.valueOf(method), path))
				.andExpect(status().isOk())
				.andExpect(content().string("reached-controller"))
				.andExpect(header().doesNotExist("WWW-Authenticate"));
	}

	@ParameterizedTest
	@ValueSource(strings = {"Basic aW52YWxpZDppbnZhbGlk", "Basic invalid", "Bearer stale-token"})
	void staleAuthorizationHeaderDoesNotTrigger401(String authorization) throws Exception {
		mockMvc.perform(get("/api/practice/tags/1").header("Authorization", authorization))
				.andExpect(status().isOk())
				.andExpect(content().string("reached-controller"));
	}

	@Test
	void otherDevelopmentRequestsAreAlsoPermitted() throws Exception {
		mockMvc.perform(get("/api/security-probe"))
				.andExpect(status().isOk())
				.andExpect(content().string("reached-controller"));
	}

	@Test
	void errorDispatchIsNotReplacedByAnAuthenticationError() throws Exception {
		mockMvc.perform(get("/api/security-probe").with(req -> {
			req.setDispatcherType(DispatcherType.ERROR);
			return req;
		})).andExpect(status().isOk());
	}
}
