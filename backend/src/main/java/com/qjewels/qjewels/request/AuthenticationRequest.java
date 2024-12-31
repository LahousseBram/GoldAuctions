package com.qjewels.qjewels.request;

import lombok.Getter;

import java.io.Serializable;


@Getter
public class AuthenticationRequest implements Serializable {

    private String email;
    private String password;

    public void setUserMail(String mail) {
        this.email = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthenticationRequest() {}

    public AuthenticationRequest(String email, String password) {
        this.setUserMail(email);
        this.setPassword(password);
    }
}