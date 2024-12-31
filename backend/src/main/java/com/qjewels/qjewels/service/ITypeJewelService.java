package com.qjewels.qjewels.service;

import com.qjewels.qjewels.dto.TypeJewelDTO;

import java.util.List;
import java.util.Optional;

public interface ITypeJewelService {
    TypeJewelDTO saveType(TypeJewelDTO typeJewelDTO);
    List<TypeJewelDTO> getAllTypes();
    Optional<TypeJewelDTO> getTypeById(Long id);
    void deleteTypeById(Long id);
}