package com.qjewels.qjewels.service.implementation;

import com.qjewels.qjewels.dto.BidDTO;
import com.qjewels.qjewels.dto.AutoBidDTO;
import com.qjewels.qjewels.mapper.BidMapper;
import com.qjewels.qjewels.mapper.AutoBidMapper;
import com.qjewels.qjewels.model.*;
import com.qjewels.qjewels.repository.*;
import com.qjewels.qjewels.service.IBidService;
import com.qjewels.qjewels.utils.messagebroker.MessageBroker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BidService implements IBidService {

    @Autowired
    private IBidRepository bidRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IJewelRepository jewelRepository;

    @Autowired
    private IAutoBidRepository autoBidRepository;

    @Override
    public List<BidDTO> getAllBids(long jewelId) {
        List<Bid> bids = bidRepository.findAllByJewel_JewelIdOrderByTimestampDesc(jewelId);
        return bids.stream()
                .map(BidMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BidDTO placeBid(BidDTO bidDto) {
        User user = userRepository.findById(bidDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Jewel jewel = jewelRepository.findById(bidDto.jewelId())
                .orElseThrow(() -> new IllegalArgumentException("Jewel not found"));
        List<Bid> bids = jewel.getBids();
        Bid lastBid = null;

        if (!bids.isEmpty()) {
            lastBid = bids.get(bids.size() - 1);

            if (lastBid.getUser().getUserId().equals(bidDto.userId())) {
                throw new IllegalStateException("You cannot place consecutive bids. Please wait for another user to bid.");
            }
        }

        if (!validateBid(bidDto, jewel.getJewelId())) {
            throw new IllegalArgumentException("Invalid bid amount");
        }

        LocalDateTime now = LocalDateTime.now();
        if (jewel.getEndDate().minusMinutes(5).isBefore(now) && jewel.getEndDate().isAfter(now)) {
            jewel.setEndDate(jewel.getEndDate().plusMinutes(5));
            jewelRepository.save(jewel);
        }

        Bid bid = BidMapper.toEntity(bidDto, user, jewel);
        Bid savedBid = bidRepository.save(bid);
        triggerAutoBids(bidDto.jewelId());

        return BidMapper.toDto(savedBid);
    }

    @Override
    public boolean validateBid(BidDTO bidDto, long jewelId) {
        Jewel jewel = jewelRepository.findById(jewelId)
                .orElseThrow(() -> new IllegalArgumentException("Jewel not found"));
        
        if (!jewel.isPublished() || jewel.getEndDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("This auction has ended");
        }

        if (bidDto.amount().longValue() < jewel.getStartingPrice().longValue()) {
            throw new IllegalArgumentException("Bid needs to be bigger than the starting amount");
        }
        
        Optional<Bid> latestBidOpt = bidRepository.findTopByJewel_JewelIdOrderByTimestampDesc(jewelId);
        if (latestBidOpt.isEmpty()) {
            return true;
        }

        Bid latestBid = latestBidOpt.get();
        BigDecimal minimumStep = getMinimumStepForAmount(latestBid.getAmount());
        return bidDto.amount().compareTo(latestBid.getAmount().add(minimumStep)) >= 0;
    }

    @Override
    public BidDTO getLatestBid(long jewelId) {
        Optional<Bid> latestBidOpt = bidRepository.findTopByJewel_JewelIdOrderByTimestampDesc(jewelId);
        if (latestBidOpt.isEmpty()) {
            return null;
        }
        return BidMapper.toDto(latestBidOpt.get());
    }

    @Override
    public void registerAutoBid(AutoBidDTO autoBidDto) {
        User user = userRepository.findById(autoBidDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Jewel jewel = jewelRepository.findById(autoBidDto.jewelId())
                .orElseThrow(() -> new IllegalArgumentException("Jewel not found"));

        AutoBid autoBid = AutoBidMapper.toEntity(autoBidDto, user, jewel);
        autoBidRepository.save(autoBid);
        triggerAutoBids(autoBidDto.jewelId());
    }

    @Override
    @Transactional
    public void triggerAutoBids(long jewelId) {
        List<AutoBid> autoBids = autoBidRepository.findAllByJewel_JewelId(jewelId);

        Jewel jewel = jewelRepository.findById(jewelId)
                .orElseThrow(() -> new IllegalArgumentException("Jewel not found"));

        Optional<Bid> latestBidOpt = bidRepository.findTopByJewel_JewelIdOrderByTimestampDesc(jewelId);
        BigDecimal currentHighestBid = latestBidOpt.map(Bid::getAmount)
                .orElse(jewel.getStartingPrice());

        boolean bidPlaced;
        do {
            bidPlaced = false;
            BigDecimal nextBidAmount = currentHighestBid.add(getMinimumStepForAmount(currentHighestBid));

            AutoBid highestBidder = null;
            for (AutoBid autoBid : autoBids) {
                if (latestBidOpt.isPresent() &&
                        latestBidOpt.get().getUser().getUserId().equals(autoBid.getUser().getUserId())) {
                    continue;
                }

                if (autoBid.getMaxAmount().compareTo(nextBidAmount) >= 0) {
                    if (highestBidder == null ||
                            autoBid.getMaxAmount().compareTo(highestBidder.getMaxAmount()) > 0) {
                        highestBidder = autoBid;
                    }
                }
            }

            if (highestBidder != null) {
                // Place the bid
                BidDTO autoBidDto = new BidDTO(
                        null,
                        nextBidAmount,
                        LocalDateTime.now(),
                        highestBidder.getUser().getUserId(),
                        highestBidder.getJewel().getJewelId(),
                        highestBidder.getUser().getUserProfile().getFirstName()
                );

                Bid newAutoBid = BidMapper.toEntity(autoBidDto, highestBidder.getUser(), highestBidder.getJewel());
                Bid savedBid = bidRepository.save(newAutoBid);

                new MessageBroker().sendMessage(BidMapper.toDto(savedBid).toString());

                currentHighestBid = nextBidAmount;
                latestBidOpt = Optional.of(savedBid);
                bidPlaced = true;
            }
        } while (bidPlaced);
    }

    private BigDecimal getMinimumStepForAmount(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("1000")) > 0) {
            return new BigDecimal("100");
        } else if (amount.compareTo(new BigDecimal("250")) >= 0) {
            return new BigDecimal("25");
        } else {
            return new BigDecimal("10");
        }
    }
}
