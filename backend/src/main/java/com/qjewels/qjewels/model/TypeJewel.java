package com.qjewels.qjewels.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Entity
public class TypeJewel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "type")
    private List<Jewel> jewels;
}