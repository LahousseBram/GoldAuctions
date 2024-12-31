package com.qjewels.qjewels.dto;

import com.qjewels.qjewels.model.Bid;
import com.qjewels.qjewels.model.enums.JewelColor;
import com.qjewels.qjewels.model.enums.JewelType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record JewelDTO(
        Long jewelId,

        @NotBlank(message = "Naam is required")
        String name,

        @NotBlank(message = "Titel is required")
        String title,

        String description,

        @NotBlank(message = "Kleur is verplicht")
        String colorName,

        @NotNull(message = "Karat is verplicht")
        @Min(value = 1, message = "Karat must be at least 1")
        @Max(value = 24, message = "Karat cannot be more than 24")
        Integer karat,

        @NotBlank(message = "Type is verplicht")
        String typeName,

        @NotNull(message = "Eind datum is verplicht")
        LocalDateTime endDate,

        @NotNull(message = "Starting price is required")
        BigDecimal startingPrice,

        boolean published,

        List<String> imageNames,
        Long auctionId,
        List<BidDTO> bids
) {}
