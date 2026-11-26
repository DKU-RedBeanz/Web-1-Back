package com.redbeanz.backend.practice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository
        extends JpaRepository<com.redbeanz.backend.practice.Tag, Long> {
}