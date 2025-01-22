package com.qjewels.qjewels.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qjewels.qjewels.model.enums.JewelColor;
import com.qjewels.qjewels.model.enums.Karat;
import com.qjewels.qjewels.model.enums.JewelType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Entity
public class Jewel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jewelId;

    private String name;

    private String title;

    @Lob
    private String description;

    private BigDecimal startingPrice;

    @ElementCollection
    private List<String> imagePaths;

    private String colorName;

    private int karat;

    private LocalDateTime endDate;
    private boolean published;

    @ElementCollection
    private List<String> imageNames;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    @JsonIgnore
    private Auction auction;

    @OneToMany(mappedBy = "jewel", cascade = CascadeType.ALL)
    private List<Bid> bids;

    @OneToMany(mappedBy = "jewel", cascade = CascadeType.ALL)
    private List<AutoBid> autoBids;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TypeJewel type;
}