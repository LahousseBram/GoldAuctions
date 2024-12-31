package com.qjewels.qjewels.controller;

import com.qjewels.qjewels.dto.JewelDTO;
import com.qjewels.qjewels.service.IAuctionService;
import com.qjewels.qjewels.service.IImageService;
import com.qjewels.qjewels.service.IJewelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jewels")
public class JewelController {

    @Autowired
    private IImageService imageService;

    private final String imageUploadDirectory = "src/main/resources/static/images/jewels";

    @Autowired
    private IJewelService jewelService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<JewelDTO> saveJewel(
            @RequestPart("jewel") @Valid JewelDTO jewelDTO,
            @RequestPart("images") List<MultipartFile> imageFiles
    ) {
        try {
            List<String> imageNames = new ArrayList<>();

            for (MultipartFile imageFile : imageFiles) {
                String imageName = imageService.saveImageToStorage(imageUploadDirectory, imageFile);
                imageNames.add(imageName);
            }

            JewelDTO updatedJewel = new JewelDTO(
                    jewelDTO.jewelId(),
                    jewelDTO.name(),
                    jewelDTO.title(),
                    jewelDTO.description(),
                    jewelDTO.colorName(),
                    jewelDTO.karat(),
                    jewelDTO.typeName(),
                    jewelDTO.endDate(),
                    jewelDTO.startingPrice(),
                    jewelDTO.published(),
                    imageNames,
                    jewelDTO.auctionId(),
                    jewelDTO.bids()
            );

            JewelDTO savedJewel = jewelService.saveJewel(updatedJewel);
            return ResponseEntity.ok(savedJewel);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<JewelDTO>> getAllJewels() {
        List<JewelDTO> jewels = jewelService.getAllJewels();
        return ResponseEntity.ok(jewels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JewelDTO> getJewelById(@PathVariable Long id) {
        Optional<JewelDTO> jewel = jewelService.getJewelById(id);
        return jewel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJewelById(@PathVariable Long id) throws IOException {
        Optional<JewelDTO> jewel = jewelService.getJewelById(id);
        if (jewel.isPresent()) {
            List<String> imageNames = jewel.get().imageNames();
            if (imageNames != null && !imageNames.isEmpty()) {
                for (String imageName : imageNames) {
                    Path imagePath = Path.of(imageUploadDirectory, imageName);
                    if (Files.exists(imagePath)) {
                        Files.delete(imagePath);
                    }
                }
            }
            jewelService.deleteJewelById(id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<byte[]> getJewelImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Path.of(imageUploadDirectory, imageName);

        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageData = Files.readAllBytes(imagePath);
        String contentType = Files.probeContentType(imagePath);

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .header("Content-Disposition", "inline; filename=\"" + imageName + "\"")
                .body(imageData);
    }
}