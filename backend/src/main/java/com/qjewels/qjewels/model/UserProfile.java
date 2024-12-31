package com.qjewels.qjewels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@Getter
@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    private String firstName;
    private String lastName;
    private String name;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String phoneNumber;
    @NotNull(message = "Terms must be accepted")
    private boolean termsAccepted;


    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JoinColumn(name = "email", nullable = false)
    private String email;

}
