package com.qjewels.qjewels.repository;

import com.qjewels.qjewels.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByJewel_JewelIdOrderByTimestampDesc(Long jewelId);

    Optional<Bid> findTopByJewel_JewelIdOrderByTimestampDesc(Long jewelId);
}
