package com.qjewels.qjewels.controller;

import com.qjewels.qjewels.dto.JewelDTO;
import com.qjewels.qjewels.dto.OrderDTO;
import com.qjewels.qjewels.service.IJewelService;
import com.qjewels.qjewels.service.IOrderService;
import com.qjewels.qjewels.utils.messagebroker.MessageBroker;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController
{
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IJewelService jewelService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<OrderDTO> saveOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO savedOrder = orderService.saveOrder(orderDTO);
        new MessageBroker().sendMessage(orderDTO.toString());
        return ResponseEntity.ok(savedOrder);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> order = orderService.getAllOrders();
        return ResponseEntity.ok(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        Optional<OrderDTO> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
