package com.qjewels.qjewels.repository;

import com.qjewels.qjewels.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findByName(String name);
}