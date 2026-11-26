package com.redbeanz.backend.practice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagCreateRequest {
    private String name;

    public TagCreateRequest() {
    }
}