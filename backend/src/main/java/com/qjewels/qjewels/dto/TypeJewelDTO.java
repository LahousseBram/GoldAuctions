package com.qjewels.qjewels.dto;

import jakarta.validation.constraints.NotBlank;

public record TypeJewelDTO(
        Long typeId,

        @NotBlank(message = "Name is required")
        String name
) {}