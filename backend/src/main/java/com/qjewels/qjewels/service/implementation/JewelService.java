package com.qjewels.qjewels.service.implementation;

import com.qjewels.qjewels.dto.BidDTO;
import com.qjewels.qjewels.dto.JewelDTO;
import com.qjewels.qjewels.mapper.JewelMapper;
import com.qjewels.qjewels.model.*;
import com.qjewels.qjewels.model.enums.OrderStatus;
import com.qjewels.qjewels.repository.*;
import com.qjewels.qjewels.service.IAuctionService;
import com.qjewels.qjewels.service.IBidService;
import com.qjewels.qjewels.service.IJewelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JewelService implements IJewelService {

    @Autowired
    private IJewelRepository jewelRepository;

    @Autowired
    private IAuctionService auctionService;

    @Autowired
    private ITypeJewelRepository typeJewelRepository;

    @Autowired
    private IBidService bidService;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IUserRepository userRepository;


    @Scheduled(fixedRate = 60000) // Runs every minute
    @Override
    public void checkExpiredJewels() {
        List<Jewel> expiredJewels = jewelRepository.findByEndDateBeforeAndPublishedTrue(LocalDateTime.now());

        for (Jewel jewel : expiredJewels) {
            if (jewel.isPublished()) {
                BidDTO highestBid = bidService.getLatestBid(jewel.getJewelId());

                if (highestBid != null) {
                    // Create order for the winner
                    JewelOrder order = new JewelOrder();
                    order.setJewel(jewel);
                    order.setPrice(highestBid.amount().longValue());
                    order.setStatus(OrderStatus.WAITING);

                    User winner = userRepository.findById(highestBid.userId())
                            .orElseThrow(() -> new IllegalStateException("Winner not found"));
                    order.setUser(winner);

                    orderRepository.save(order);
                }

                jewelRepository.save(jewel);
            }
        }
    }

    @Override
    public JewelDTO saveJewel(JewelDTO jewelDTO) {
        Optional<Auction> auction = auctionService.getAuctionById(jewelDTO.auctionId());

        TypeJewel type = typeJewelRepository.findByName(jewelDTO.typeName())
                .orElseThrow(() -> new IllegalArgumentException("Type not found: " + jewelDTO.typeName()));

        Jewel jewel = JewelMapper.toEntity(jewelDTO, auction, type);
        Jewel savedJewel = jewelRepository.save(jewel);
        return JewelMapper.toDto(savedJewel);
    }

    @Override
    public List<JewelDTO> getAllJewels() {
        return jewelRepository.findAll()
                .stream()
                .map(JewelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JewelDTO> getJewelById(Long id) {
        return jewelRepository.findById(id)
                .map(JewelMapper::toDto);
    }

    @Override
    public void deleteJewelById(Long id) {
        jewelRepository.deleteById(id);
    }
}
