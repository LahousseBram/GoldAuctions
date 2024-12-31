package com.qjewels.qjewels.controller;

import com.qjewels.qjewels.dto.AuctionDTO;
import com.qjewels.qjewels.model.Auction;
import com.qjewels.qjewels.model.enums.AuctionType;
import com.qjewels.qjewels.model.enums.JewelColor;
import com.qjewels.qjewels.service.IAuctionService;
import com.qjewels.qjewels.service.IImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {
    @Autowired
    private IAuctionService auctionService;

    @Autowired
    private IImageService imageService;

    private final String imageUploadDirectory = "src/main/resources/static/images/auctions";

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Auction>> getAllAuctions() {
        List<Auction> auctions = auctionService.getAllAuctions();
        return ResponseEntity.ok(auctions);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable long id) {
        Optional<Auction> auction = auctionService.getAuctionById(id);
        return auction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuctionById(@PathVariable Long id) throws IOException {
        Optional<Auction> auction = auctionService.getAuctionById(id);
        if (auction.isPresent()) {
            String imageName = auction.get().getImageName();
            imageService.deleteImage(imageUploadDirectory, imageName);
            auctionService.deleteAuctionById(id);
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<AuctionDTO> saveAuction(
            @RequestPart("auction") @Valid AuctionDTO auctionDTO,
            @RequestPart("image") MultipartFile imageFile
    ) {
        try {
            String uploadDirectory = "src/main/resources/static/images/auctions";
            String imageName = imageService.saveImageToStorage(uploadDirectory, imageFile);

            AuctionDTO updatedAuction = new AuctionDTO(
                    auctionDTO.auctionId(),
                    auctionDTO.naam(),
                    auctionDTO.type(),
                    auctionDTO.startDate(),
                    auctionDTO.endDate(),
                    auctionDTO.published(),
                    auctionDTO.description(),
                    imageName
            );

            AuctionDTO savedAuction = auctionService.saveAuction(updatedAuction);
            return ResponseEntity.ok(savedAuction);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<byte[]> getAuctionImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Path.of(imageUploadDirectory, imageName);

        if (!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageData = Files.readAllBytes(imagePath);

        String contentType = Files.probeContentType(imagePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                .body(imageData);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/types")
    public ResponseEntity<List<AuctionType>> getAuctionTypes() {
        return ResponseEntity.ok(List.of(AuctionType.values()));
    }
}
