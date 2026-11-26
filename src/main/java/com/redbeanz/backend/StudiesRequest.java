package com.redbeanz.backend;
import lombok.Getter;
@Getter
public class StudiesRequest {
    private final String title;
    private final String description;

    public StudiesRequest(
            String title, String description) {
        this.title = title;
        this.description = description;
    }
}