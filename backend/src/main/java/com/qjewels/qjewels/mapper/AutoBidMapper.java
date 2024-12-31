package com.qjewels.qjewels.mapper;

import com.qjewels.qjewels.dto.AutoBidDTO;
import com.qjewels.qjewels.model.AutoBid;
import com.qjewels.qjewels.model.Jewel;
import com.qjewels.qjewels.model.User;

public class AutoBidMapper {
    public static AutoBidDTO toDto(AutoBid autoBid) {
        return new AutoBidDTO(
                autoBid.getAutoBidId(),
                autoBid.getUser().getUserId(),
                autoBid.getJewel().getJewelId(),
                autoBid.getMaxAmount()
        );
    }

    public static AutoBid toEntity(AutoBidDTO autoBidDTO, User user, Jewel jewel) {
        AutoBid autoBid = new AutoBid();
        autoBid.setAutoBidId(autoBidDTO.autoBidId());
        autoBid.setUser(user);
        autoBid.setJewel(jewel);
        autoBid.setMaxAmount(autoBidDTO.maxAmount());
        return autoBid;
    }
}
