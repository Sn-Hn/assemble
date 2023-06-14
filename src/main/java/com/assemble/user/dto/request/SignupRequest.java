package com.assemble.user.dto.request;

public class SignupRequest {
    private String email;

    private String name;

    private String nickname;

    private String phoneNumber;

    private String password;

    public SignupRequest(String email, String name, String nickname, String phoneNumber, String password) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
