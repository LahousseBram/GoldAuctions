package com.qjewels.qjewels.dto;

import lombok.Getter;

@Getter
public class ContactEmailDTO {
    private String name;
    private String email;
    private String message;

    public ContactEmailDTO(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
    }

}
