package com.qjewels.qjewels.service.implementation;

import com.qjewels.qjewels.dto.TypeJewelDTO;
import com.qjewels.qjewels.model.TypeJewel;
import com.qjewels.qjewels.repository.ITypeJewelRepository;
import com.qjewels.qjewels.service.ITypeJewelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TypeJewelService implements ITypeJewelService {

    @Autowired
    private ITypeJewelRepository typeJewelRepository;

    @Override
    public TypeJewelDTO saveType(TypeJewelDTO typeJewelDTO) {
        TypeJewel type = toEntity(typeJewelDTO);
        TypeJewel savedType = typeJewelRepository.save(type);
        return toDto(savedType);
    }

    @Override
    public List<TypeJewelDTO> getAllTypes() {
        return typeJewelRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TypeJewelDTO> getTypeById(Long id) {
        return typeJewelRepository.findById(id)
                .map(this::toDto);
    }

    @Override
    public void deleteTypeById(Long id) {
        typeJewelRepository.deleteById(id);
    }

    private TypeJewelDTO toDto(TypeJewel type) {
        return new TypeJewelDTO(
                type.getTypeId(),
                type.getName()
        );
    }

    private TypeJewel toEntity(TypeJewelDTO dto) {
        TypeJewel type = new TypeJewel();
        type.setTypeId(dto.typeId());
        type.setName(dto.name());
        return type;
    }
}