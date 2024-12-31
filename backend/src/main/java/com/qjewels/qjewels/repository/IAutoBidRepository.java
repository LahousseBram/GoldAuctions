package com.qjewels.qjewels.repository;

import com.qjewels.qjewels.model.AutoBid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAutoBidRepository extends JpaRepository<AutoBid, Long> {
    List<AutoBid> findAllByJewel_JewelId(long jewelId);
}
