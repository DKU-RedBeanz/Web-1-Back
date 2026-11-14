package com.redbeanz.backend;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StudiesRepository
        extends JpaRepository<Studies, Long> {
}