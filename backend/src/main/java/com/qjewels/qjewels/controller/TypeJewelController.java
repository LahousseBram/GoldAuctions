package com.qjewels.qjewels.controller;

import com.qjewels.qjewels.dto.TypeJewelDTO;
import com.qjewels.qjewels.service.ITypeJewelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jewel-types")
public class TypeJewelController {

    @Autowired
    private ITypeJewelService typeJewelService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TypeJewelDTO> saveType(@Valid @RequestBody TypeJewelDTO typeJewelDTO) {
        TypeJewelDTO savedType = typeJewelService.saveType(typeJewelDTO);
        return ResponseEntity.ok(savedType);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TypeJewelDTO>> getAllTypes() {
        List<TypeJewelDTO> types = typeJewelService.getAllTypes();
        return ResponseEntity.ok(types);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TypeJewelDTO> getTypeById(@PathVariable Long id) {
        Optional<TypeJewelDTO> type = typeJewelService.getTypeById(id);
        return type.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeById(@PathVariable Long id) {
        typeJewelService.deleteTypeById(id);
        return ResponseEntity.noContent().build();
    }
}