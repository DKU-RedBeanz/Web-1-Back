package com.redbeanz.backend;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class StudiesUpdateRequest {
    @NotBlank @Size(max = 100)
    private String title;
    @NotNull @Size(max = 255)
    private String description;
    public StudiesUpdateRequest() {
    }
}