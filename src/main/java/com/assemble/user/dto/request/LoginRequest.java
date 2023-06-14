package com.assemble.user.dto.request;

public class LoginRequest {

    private String email;

    private String pasword;

    public LoginRequest(String email, String pasword) {
        this.email = email;
        this.pasword = pasword;
    }

    public String getEmail() {
        return email;
    }

    public String getPasword() {
        return pasword;
    }
}
