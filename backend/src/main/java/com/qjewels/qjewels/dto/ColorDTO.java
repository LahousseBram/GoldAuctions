package com.qjewels.qjewels.dto;

import jakarta.validation.constraints.NotBlank;

public record ColorDTO(
        Long colorId,

        @NotBlank(message = "Name is required")
        String name
) {}