package com.qjewels.qjewels.dto;

import com.qjewels.qjewels.model.UserProfile;
import com.qjewels.qjewels.model.enums.Role;
import lombok.Getter;


@Getter
public class UserProfileDTO {

    private Long id;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String phoneNumber;
    private String email;
    private Role role;
    private String name;

    public UserProfileDTO() {
    }
    public UserProfileDTO(UserProfile userProfile) {
        this.id = userProfile.getUser().getUserId();
        this.street = userProfile.getStreet();
        this.city = userProfile.getCity();
        this.postalCode = userProfile.getPostalCode();
        this.country = userProfile.getCountry();
        this.phoneNumber = userProfile.getPhoneNumber();
        this.email = userProfile.getEmail();
        this.role = userProfile.getUser().getUserRole();
        this.name = userProfile.getUser().getName();
    }

}
