package com.qjewels.qjewels.dto;

import com.qjewels.qjewels.model.Jewel;
import com.qjewels.qjewels.model.enums.AuctionType;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public record AuctionDTO (
    Long auctionId,
    @NotBlank(message = "Naam is required")
    String naam,
    @NotNull(message = "Type is verplicht")
    AuctionType type,
    @NotNull(message = "Startdatum is verplicht")
    LocalDateTime startDate,
    @NotNull(message = "Einddatum is verplicht")
    LocalDateTime endDate,
    @NotNull(message = "Published is verplicht")
    boolean published,
    @ElementCollection
    @NotBlank(message = "korte beschrijving is verplicht")
    String description,
    String imageName
) {}
