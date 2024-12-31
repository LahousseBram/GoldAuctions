package com.qjewels.qjewels.mapper;

import com.qjewels.qjewels.dto.BidDTO;
import com.qjewels.qjewels.model.Bid;
import com.qjewels.qjewels.model.Jewel;
import com.qjewels.qjewels.model.User;

import java.time.LocalDateTime;

public class BidMapper {
    public static BidDTO toDto(Bid bid) {
        return new BidDTO(
                bid.getBidId(),
                bid.getAmount(),
                bid.getTimestamp(),
                bid.getUser().getUserId(),
                bid.getJewel().getJewelId(),
                bid.getUser().getName()
        );
    }

    public static Bid toEntity(BidDTO bidDTO, User user, Jewel jewel) {
        Bid bid = new Bid();
        bid.setBidId(bidDTO.bidId());
        bid.setAmount(bidDTO.amount());
        bid.setTimestamp(LocalDateTime.now());
        bid.setUser(user);
        bid.setJewel(jewel);
        return bid;
    }
}
