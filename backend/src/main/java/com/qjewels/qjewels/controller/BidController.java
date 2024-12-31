package com.qjewels.qjewels.controller;

import com.qjewels.qjewels.dto.BidDTO;
import com.qjewels.qjewels.dto.AutoBidDTO;
import com.qjewels.qjewels.service.IBidService;
import com.qjewels.qjewels.utils.messagebroker.MessageBroker;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bids")
public class BidController {

    @Autowired
    private IBidService bidService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{jewelId}")
    public ResponseEntity<List<BidDTO>> getAllBids(@PathVariable long jewelId) {
        List<BidDTO> bids = bidService.getAllBids(jewelId);
        return ResponseEntity.ok(bids);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BidDTO> placeBid(@Valid @RequestBody BidDTO bidDto) {
        BidDTO placedBid = bidService.placeBid(bidDto);
        new MessageBroker().sendMessage(placedBid.toString());
        return ResponseEntity.ok(placedBid);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/latest/{jewelId}")
    public ResponseEntity<BidDTO> getLatestBid(@PathVariable long jewelId) {
        BidDTO latestBid = bidService.getLatestBid(jewelId);
        if (latestBid != null) {
            return ResponseEntity.ok(latestBid);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/register-autobid")
    public ResponseEntity<Void> registerAutoBid(@Valid @RequestBody AutoBidDTO autoBidDto) {
        bidService.registerAutoBid(autoBidDto);
        return ResponseEntity.ok().build();
    }
}
