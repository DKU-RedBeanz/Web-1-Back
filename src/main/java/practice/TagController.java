package com.redbeanz.backend.practice;

import java.util.Optional;
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
}
