package com.qjewels.qjewels.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Entity
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long colorId;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "color")
    private List<Jewel> jewels;
}
