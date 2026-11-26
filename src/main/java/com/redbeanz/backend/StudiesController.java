package com.redbeanz.backend;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/practice/studies")
public class StudiesController {
    private final StudiesService studiesService;

    public StudiesController(StudiesService studiesService) {

        this.studiesService = studiesService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudiesResponse> find(
            @PathVariable("id") Long id) {
        Optional<StudiesResponse> result = studiesService.find(id);
        if (result.isPresent()) {
            StudiesResponse response = result.get();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<StudiesResponse>> findAll() {
        List<StudiesResponse> responses = studiesService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<StudiesResponse> update(@PathVariable("id") Long id,
                                     @Valid @RequestBody StudiesRequest request) {
        return ResponseEntity.ok(studiesService.update(id, request));
    }
}
