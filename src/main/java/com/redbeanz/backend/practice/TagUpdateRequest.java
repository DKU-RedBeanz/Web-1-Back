package com.redbeanz.backend.practice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagUpdateRequest {
    private String name;

    public TagUpdateRequest() {
    }
}