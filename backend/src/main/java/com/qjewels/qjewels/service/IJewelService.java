package com.qjewels.qjewels.service;

import com.qjewels.qjewels.dto.JewelDTO;
import com.qjewels.qjewels.model.Jewel;

import java.util.List;
import java.util.Optional;

public interface IJewelService {
    JewelDTO saveJewel(JewelDTO jewelDTO);
    List<JewelDTO> getAllJewels();
    Optional<JewelDTO> getJewelById(Long id);
    void deleteJewelById(Long id);
    void checkExpiredJewels();
}
