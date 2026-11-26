package com.redbeanz.backend.practice;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

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
        Optional<com.redbeanz.backend.practice.Tag> result =
                tagRepository.findById(id);

        if (result.isPresent()) {
            com.redbeanz.backend.practice.Tag tag = result.get();
            com.redbeanz.backend.practice.TagResponse response =
                    new com.redbeanz.backend.practice.TagResponse(
                            tag.getId(), tag.getName()
                    );
            return Optional.of(response);
        }

        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<TagResponse> findAll() {
        List<Tag> tags = tagRepository.findAll();
        List<TagResponse> responses = new ArrayList<>();
        for (Tag tag : tags) {
            TagResponse response = new TagResponse(tag.getId(), tag.getName());
            responses.add(response);
        }
        return responses;
    }

    @Transactional
    public TagResponse create(TagCreateRequest request) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        Tag saved = tagRepository.save(tag);
        return new TagResponse(saved.getId(), saved.getName());
    }

    @Transactional
    public Optional<TagResponse> update(Long id, TagUpdateRequest request) {
        Optional<Tag> result = tagRepository.findById(id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Tag tag = result.get();
        tag.setName(request.getName());
        TagResponse response = new TagResponse(tag.getId(), tag.getName());
        return Optional.of(response);
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<Tag> result = tagRepository.findById(id);
        if (result.isEmpty()) {
            return false;
        }
        Tag tag = result.get();
        tagRepository.delete(tag);
        return true;
    }
}