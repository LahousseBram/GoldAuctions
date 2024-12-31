package com.qjewels.qjewels.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BidDTO(
        Long bidId,

        @NotNull(message = "amount required")
        BigDecimal amount,
        LocalDateTime timestamp,

        @NotNull(message = "user id is required")
        Long userId,

        @NotNull(message = "jewel id is required")
        Long jewelId,
        String username
) {

        public String toString() {
                return "{ \"amount\": " + this.amount + ",\"userId\": " + this.userId + ", \"jewelId\": " + this.jewelId + ", \"username\": \"" + this.username + "\" }";
        }
}
