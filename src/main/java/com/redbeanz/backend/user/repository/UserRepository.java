package com.redbeanz.backend.user.repository;

import com.redbeanz.backend.user.entity.User;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByLoginId(String loginId);

	boolean existsByEmail(String email);
	boolean existsByEmailAndIdNot(String email, Long id);

	Optional<User> findByLoginId(String loginId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<User> findForUpdateByLoginId(String loginId);
}
