package com.qjewels.qjewels.service.implementation;

import com.qjewels.qjewels.dto.OrderDTO;
import com.qjewels.qjewels.mapper.OrderMapper;
import com.qjewels.qjewels.model.*;
import com.qjewels.qjewels.repository.*;
import com.qjewels.qjewels.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IJewelRepository jewelRepository;

    @Override
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        if (orderRepository.existsByJewelJewelId(orderDTO.jewelId())) {
            throw new IllegalStateException("This jewel already has an existing order");
        }

        User user = userRepository.findById(orderDTO.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Jewel jewel = jewelRepository.findById(orderDTO.jewelId())
                .orElseThrow(() -> new IllegalArgumentException("Jewel not found"));

        JewelOrder order = OrderMapper.toEntity(orderDTO, user, jewel);
        JewelOrder savedOrder = orderRepository.save(order);
        return OrderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toDto);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }
}
