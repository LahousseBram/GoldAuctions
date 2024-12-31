package com.qjewels.qjewels.repository;

import com.qjewels.qjewels.model.TypeJewel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITypeJewelRepository extends JpaRepository<TypeJewel, Long> {
    Optional<TypeJewel> findByName(String name);
}