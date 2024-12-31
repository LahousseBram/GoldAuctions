package com.qjewels.qjewels.mapper;

import com.qjewels.qjewels.dto.AuctionDTO;
import com.qjewels.qjewels.model.Auction;

import java.util.stream.Collectors;

public class AuctionMapper {
    public static AuctionDTO toDto(Auction auction) {
        return new AuctionDTO(
                auction.getAuctionId(),
                auction.getName(),
                auction.getType(),
                auction.getStartDate(),
                auction.getEndDate(),
                auction.isPublished(),
                auction.getDescription(),
                auction.getImageName()
        );
    }

    public static Auction toEntity(AuctionDTO auctionDTO) {
        Auction auction = new Auction();
        auction.setAuctionId(auctionDTO.auctionId());
        auction.setName(auctionDTO.naam());
        auction.setType(auctionDTO.type());
        auction.setDescription(auctionDTO.description());
        auction.setEndDate(auctionDTO.endDate());
        auction.setPublished(auctionDTO.published());
        auction.setStartDate(auctionDTO.startDate());
        auction.setImageName(auctionDTO.imageName());
        return auction;
    }

}
