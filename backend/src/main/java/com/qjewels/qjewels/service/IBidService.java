package com.qjewels.qjewels.service;

import com.qjewels.qjewels.dto.AutoBidDTO;
import com.qjewels.qjewels.dto.BidDTO;

import java.util.List;

public interface IBidService {
    List<BidDTO> getAllBids(long jewelId);
    BidDTO placeBid(BidDTO bidDto);
    boolean validateBid(BidDTO bidDto, long jewelId);
    BidDTO getLatestBid(long jewelId);
    void registerAutoBid(AutoBidDTO autoBidDto);
    void triggerAutoBids(long jewelId);

}
