package com.qjewels.qjewels.service;

import com.qjewels.qjewels.dto.OrderDTO;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    OrderDTO saveOrder(OrderDTO orderDTO);
    List<OrderDTO> getAllOrders();
    Optional<OrderDTO> getOrderById(Long id);
    void deleteOrderById(Long id);
}
