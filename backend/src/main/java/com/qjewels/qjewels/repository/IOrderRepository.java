package com.qjewels.qjewels.repository;

import com.qjewels.qjewels.model.JewelOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<JewelOrder,Long> {
    boolean existsByJewelJewelId(Long jewelId);
}
