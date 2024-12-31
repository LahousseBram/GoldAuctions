package com.qjewels.qjewels.repository;

import com.qjewels.qjewels.model.Jewel;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IJewelRepository extends JpaRepository<Jewel,Long> {
        List<Jewel> findByEndDateBeforeAndPublishedTrue(LocalDateTime now);

}
