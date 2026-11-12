package com.redbeanz.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DeleteUserRequest(
		@NotNull @Positive Long id,
		@NotBlank @Size(max = 64) String password
) {
}
