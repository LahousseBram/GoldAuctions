package com.qjewels.qjewels.service;

import com.qjewels.qjewels.dto.AuctionDTO;
import com.qjewels.qjewels.model.Auction;

import java.util.List;
import java.util.Optional;

public interface IAuctionService {


    AuctionDTO saveAuction(AuctionDTO auction);


    List<Auction> getAllAuctions();


    Optional<Auction> getAuctionById(Long id);


    void deleteAuctionById(Long id);


}
