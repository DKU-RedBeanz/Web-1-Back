package com.redbeanz.backend.security;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 실제 CRUD 구현 여부나 DB 상태와 독립적으로 보안 필터 통과를 확인합니다. */
@RestController
@Profile("security-probe")
class SecurityProbeController {

	@RequestMapping({"/api/practice/studies", "/api/practice/studies/{id}",
			"/api/practice/tags", "/api/practice/tags/{id}", "/api/security-probe"})
	String probe() {
		return "reached-controller";
	}
}
