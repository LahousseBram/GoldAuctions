package com.qjewels.qjewels.repository;

import com.qjewels.qjewels.dto.AuctionDTO;
import com.qjewels.qjewels.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuctionRepository extends JpaRepository<Auction,Long> {
}
