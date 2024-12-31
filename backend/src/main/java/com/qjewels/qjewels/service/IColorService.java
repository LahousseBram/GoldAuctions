package com.qjewels.qjewels.service;

import com.qjewels.qjewels.dto.ColorDTO;

import java.util.List;
import java.util.Optional;

public interface IColorService {
    ColorDTO saveColor(ColorDTO colorDTO);
    List<ColorDTO> getAllColors();
    Optional<ColorDTO> getColorById(Long id);
    void deleteColorById(Long id);
}
