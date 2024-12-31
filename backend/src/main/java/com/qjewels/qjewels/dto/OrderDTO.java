package com.qjewels.qjewels.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.qjewels.qjewels.model.enums.OrderStatus;

public record OrderDTO(
        Long orderId,

        @NotNull(message = "user id is required")
        Long userId,

        @NotNull(message = "jewel id is required")
        Long jewelId,

        Long price,

        OrderStatus status
) {
}