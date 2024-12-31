package com.qjewels.qjewels.controller;

import com.qjewels.qjewels.model.enums.AuctionType;
import com.qjewels.qjewels.model.enums.JewelColor;
import com.qjewels.qjewels.model.enums.JewelType;
import com.qjewels.qjewels.model.enums.Karat;
import com.qjewels.qjewels.model.enums.OrderStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/jewel")
public class JewelDataController {

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/color")
    public ResponseEntity<List<JewelColor>> getJewelColor() {
        return ResponseEntity.ok(List.of(JewelColor.values()));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/auction-type")
    public ResponseEntity<List<AuctionType>> getAuctionType() {
        return ResponseEntity.ok(List.of(AuctionType.values()));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/jewel-type")
    public ResponseEntity<List<JewelType>> getJewelType() {
        return ResponseEntity.ok(List.of(JewelType.values()));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/karat")
    public ResponseEntity<List<Karat>> getKarat() {
        return ResponseEntity.ok(List.of(Karat.values()));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/order-status")
    public ResponseEntity<List<OrderStatus>> getOrderStatus() {
        return ResponseEntity.ok(List.of(OrderStatus.values()));
    }



}
