package com.qjewels.qjewels.model;

import com.qjewels.qjewels.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Entity
public class JewelOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.WAITING;

    private Long price;

    @OneToOne
    @JoinColumn(name = "jewel_id")
    private Jewel jewel;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
