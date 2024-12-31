package com.qjewels.qjewels.mapper;

import com.qjewels.qjewels.dto.OrderDTO;
import com.qjewels.qjewels.model.Bid;
import com.qjewels.qjewels.model.JewelOrder;
import com.qjewels.qjewels.service.IJewelService;
import com.qjewels.qjewels.service.implementation.JewelService;
import com.qjewels.qjewels.service.implementation.BidService;
import com.qjewels.qjewels.service.IBidService;
import com.qjewels.qjewels.model.User;
import com.qjewels.qjewels.model.Jewel;

import java.util.List;

public class OrderMapper {
    public static OrderDTO toDto(JewelOrder order) {
        Jewel jewel = order.getJewel();
        List<Bid> bids = jewel.getBids();
        Long price = doesJewelHaveBids(jewel) && !bids.isEmpty() ?
                bids.get(bids.size() - 1).getAmount().longValue() :
                jewel.getStartingPrice().longValue();

        return new OrderDTO(
                order.getOrderId(),
                order.getUser().getUserId(),
                order.getJewel().getJewelId(),
                price,
                order.getStatus()
        );
    }

    public static JewelOrder toEntity(OrderDTO orderDTO, User user, Jewel jewel) {
        JewelOrder order = new JewelOrder();
        order.setOrderId(orderDTO.orderId());
        order.setUser(user);
        order.setJewel(jewel);

        // Set price based on highest bid or starting price
        if (doesJewelHaveBids(jewel)) {
            order.setPrice(jewel.getBids().get(jewel.getBids().size() - 1).getAmount().longValue());
        } else {
            order.setPrice(jewel.getStartingPrice().longValue());
        }

        return order;
    }

    private static boolean doesJewelHaveBids(Jewel jewel) {
        return jewel.getBids() != null && !jewel.getBids().isEmpty();
    }
}