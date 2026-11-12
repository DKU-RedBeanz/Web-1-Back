package com.redbeanz.backend.practice;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {

    private final com.redbeanz.backend.practice.TagRepository tagRepository;

    public TagService(com.redbeanz.backend.practice.TagRepository repository) {
        this.tagRepository = repository;
    }

    @Transactional(readOnly = true)
    public Optional<com.redbeanz.backend.practice.TagResponse> find(Long id) {
        Optional<com.redbeanz.backend.practice.Tag> result = tagRepository.findById(id);

        if (result.isPresent()) {
            com.redbeanz.backend.practice.Tag tag = result.get();
            com.redbeanz.backend.practice.TagResponse response = new com.redbeanz.backend.practice.TagResponse(
                    tag.getId(), tag.getName()
            );
            return Optional.of(response);
        }

        return Optional.empty();
    }
}