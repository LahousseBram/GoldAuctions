package com.qjewels.qjewels.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    private BigDecimal amount;

    private LocalDateTime timestamp;

    private BigDecimal autoBidMax;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "jewel_id", nullable = false)
    private Jewel jewel;

}
