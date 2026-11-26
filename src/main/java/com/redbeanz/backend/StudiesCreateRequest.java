package com.redbeanz.backend;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class StudiesCreateRequest {
    @NotBlank @Size(max = 100)
    private String title;
    @NotBlank @Size(max = 255)
    private String summary;
    @Size(max = 255)
    private String description;
    @NotBlank @Size(max = 30)
    private String goal;
    @NotBlank @Size(max = 30)
    private String level;
    @NotBlank @Size(max = 30)
    private String time;
    @NotBlank @Size(max = 30)
    private String mode;
    @Size(max = 30)
    private String region;
    @Size(max = 100)
    private String location;
    @NotNull @Positive
    private Long capacity;
    @NotBlank @Size(max = 50)
    private String leader_nickname;
    public StudiesCreateRequest() {
    }
}