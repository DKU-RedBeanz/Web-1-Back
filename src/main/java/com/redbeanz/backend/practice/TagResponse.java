package com.redbeanz.backend.practice;

import lombok.Getter;

@Getter
public class TagResponse {

    private final Long id;
    private final String name;

    public TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}