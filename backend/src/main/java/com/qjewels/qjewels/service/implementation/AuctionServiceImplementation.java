package com.qjewels.qjewels.service.implementation;

import com.qjewels.qjewels.dto.AuctionDTO;
import com.qjewels.qjewels.mapper.AuctionMapper;
import com.qjewels.qjewels.model.Auction;
import com.qjewels.qjewels.repository.IAuctionRepository;
import com.qjewels.qjewels.service.IAuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionServiceImplementation implements IAuctionService {
    @Autowired
    private IAuctionRepository auctionRepository;

    @Override
    public AuctionDTO saveAuction(AuctionDTO auctionDTO) {
        Auction auction = AuctionMapper.toEntity(auctionDTO);
        Auction savedAuction = auctionRepository.save(auction);
        return AuctionMapper.toDto(savedAuction);
    }

    @Override
    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    @Override
    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    @Override
    public void deleteAuctionById(Long id) {
        auctionRepository.delete(auctionRepository.getReferenceById(id));
    }
}
