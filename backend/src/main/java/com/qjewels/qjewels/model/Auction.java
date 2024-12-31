package com.qjewels.qjewels.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qjewels.qjewels.model.enums.AuctionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
@Getter
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionId;

    private String name;

    @Enumerated(EnumType.STRING)
    private AuctionType type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean published;

    @Lob
    private String description;

    private String shortDescription;
    private String imageName;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"bids", "autoBids", "type", "color"})
    private List<Jewel> jewels;


}
