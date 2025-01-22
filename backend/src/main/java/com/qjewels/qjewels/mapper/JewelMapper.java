package com.qjewels.qjewels.mapper;

import com.qjewels.qjewels.dto.BidDTO;
import com.qjewels.qjewels.dto.JewelDTO;
import com.qjewels.qjewels.model.Auction;
import com.qjewels.qjewels.model.Jewel;
import com.qjewels.qjewels.model.TypeJewel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JewelMapper {

    public static JewelDTO toDto(Jewel jewel) {
        List<BidDTO> bidDTOs = jewel.getBids() != null
                ? jewel.getBids().stream()
                .map(BidMapper::toDto)
                .collect(Collectors.toList())
                : List.of();

        return new JewelDTO(
                jewel.getJewelId(),
                jewel.getName(),
                jewel.getTitle(),
                jewel.getDescription(),
                jewel.getColorName(),
                jewel.getKarat(),
                jewel.getType() != null ? jewel.getType().getName() : null,
                jewel.getEndDate(),
                jewel.getStartingPrice(),
                jewel.isPublished(),
                jewel.getImageNames(),
                jewel.getAuction() != null ? jewel.getAuction().getAuctionId() : null,
                bidDTOs
        );
    }

    public static Jewel toEntity(JewelDTO jewelDTO, Optional<Auction> auction, TypeJewel type) {
        Jewel jewel = new Jewel();
        jewel.setJewelId(jewelDTO.jewelId());
        jewel.setName(jewelDTO.name());
        jewel.setTitle(jewelDTO.title());
        jewel.setDescription(jewelDTO.description());
        jewel.setImageNames(jewelDTO.imageNames());
        jewel.setKarat(jewelDTO.karat());
        jewel.setType(type);
        jewel.setEndDate(jewelDTO.endDate());
        jewel.setStartingPrice(jewelDTO.startingPrice());
        jewel.setPublished(jewelDTO.published());
        jewel.setAuction(auction.orElse(null));
        jewel.setColorName(jewelDTO.colorName());
        return jewel;
    }
}
