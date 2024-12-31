package com.qjewels.qjewels.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserDTO {

    @NotBlank(message = "Name is required.")
    private String name;
    @Email(message = "Please provide a valid email address.")
    @NotBlank(message = "Email is required.")
    private String email;
    private String token;


    public UserDTO() {
    }

    public UserDTO( String name, String email, String token) {
        this.name = name;
        this.email = email;
        this.token = token;

    }


    public void setName(String name) {
        this.name = name;
    }

}
