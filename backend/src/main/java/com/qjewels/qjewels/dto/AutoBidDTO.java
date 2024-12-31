package com.qjewels.qjewels.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AutoBidDTO(
        Long autoBidId,

        @NotNull(message = "user id is required")
        Long userId,

        @NotNull(message = "jewel id is required")
        Long jewelId,

        @NotNull(message = "max amount is required")
        BigDecimal maxAmount
) {
}
