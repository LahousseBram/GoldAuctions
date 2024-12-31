package com.qjewels.qjewels.dto;

public class ResetPasswordDTO {

    private String token;
    private String newPassword;

    public ResetPasswordDTO(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }
    public String getToken() {
        return token;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
