package com.qjewels.qjewels.controller;

import com.qjewels.qjewels.dto.ColorDTO;
import com.qjewels.qjewels.service.IColorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/colors")
public class ColorController {

    @Autowired
    private IColorService colorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ColorDTO> saveColor(@Valid @RequestBody ColorDTO colorDTO) {
        ColorDTO savedColor = colorService.saveColor(colorDTO);
        return ResponseEntity.ok(savedColor);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ColorDTO>> getAllColors() {
        List<ColorDTO> colors = colorService.getAllColors();
        return ResponseEntity.ok(colors);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ColorDTO> getColorById(@PathVariable Long id) {
        Optional<ColorDTO> color = colorService.getColorById(id);
        return color.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColorById(@PathVariable Long id) {
        colorService.deleteColorById(id);
        return ResponseEntity.noContent().build();
    }
}