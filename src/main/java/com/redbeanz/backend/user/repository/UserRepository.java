package com.redbeanz.backend.user.repository;

import com.redbeanz.backend.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByLoginId(String loginId);

	boolean existsByEmail(String email);

	// 로그인(Back #8)에서 사용합니다.
	Optional<User> findByLoginId(String loginId);
}
