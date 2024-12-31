package com.qjewels.qjewels.service.implementation;

import com.qjewels.qjewels.dto.ColorDTO;
import com.qjewels.qjewels.model.Color;
import com.qjewels.qjewels.repository.IColorRepository;
import com.qjewels.qjewels.service.IColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColorService implements IColorService {

    @Autowired
    private IColorRepository colorRepository;

    @Override
    public ColorDTO saveColor(ColorDTO colorDTO) {
        Color color = toEntity(colorDTO);
        Color savedColor = colorRepository.save(color);
        return toDto(savedColor);
    }

    @Override
    public List<ColorDTO> getAllColors() {
        return colorRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ColorDTO> getColorById(Long id) {
        return colorRepository.findById(id)
                .map(this::toDto);
    }

    @Override
    public void deleteColorById(Long id) {
        colorRepository.deleteById(id);
    }

    private ColorDTO toDto(Color color) {
        return new ColorDTO(
                color.getColorId(),
                color.getName()
        );
    }

    private Color toEntity(ColorDTO dto) {
        Color color = new Color();
        color.setColorId(dto.colorId());
        color.setName(dto.name());
        return color;
    }
}