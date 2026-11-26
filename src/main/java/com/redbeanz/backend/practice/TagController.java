package com.redbeanz.backend.practice;

import java.util.Optional;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/practice/tags")

public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> find(
            @PathVariable("id") Long id) {

        Optional<TagResponse> result = tagService.find(id);

        if (result.isPresent()) {
            TagResponse response = result.get();
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> findAll() {
        List<TagResponse> responses = tagService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<TagResponse> create(
            @RequestBody TagCreateRequest request) {
        TagResponse response = tagService.create(request);
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TagResponse> update(@PathVariable("id") Long id,
                                              @RequestBody TagUpdateRequest request) {
        Optional<TagResponse> result = tagService.update(id, request);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean deleted = tagService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}