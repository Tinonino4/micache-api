package com.api.micache_api.security.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String username,
                               @NotBlank String password) {
}
