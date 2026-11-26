package com.redbeanz.backend;

import java.net.URI;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/practice/studies")
public class StudiesController {
    private final StudiesService studiesService;

    public StudiesController(StudiesService studiesService) {

        this.studiesService = studiesService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudiesResponse> find(@PathVariable("id") Long id) {
        Optional<StudiesResponse> result = studiesService.find(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<StudiesResponse>> findAll() {
        List<StudiesResponse> responses = studiesService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<StudiesResponse> create(
            @Valid @RequestBody StudiesCreateRequest request) {
        StudiesResponse response = studiesService.create(request);
        URI location = URI.create("/api/practice/studies/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudiesResponse> update(@PathVariable("id") Long id,
                                                  @Valid @RequestBody StudiesUpdateRequest request) {
        Optional<StudiesResponse> result = studiesService.update(id, request);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean deleted = studiesService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
