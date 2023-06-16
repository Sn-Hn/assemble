package com.assemble.user.dto.response;

import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private Long userId;

    private String email;

    private String name;

    private String nickname;

    private String phoneNumber;

    private String role;

    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.getId(),
                user.getEmail().getValue(),
                user.getName().getValue(),
                user.getNickName(),
                user.getPhoneNumber().getValue(),
                user.getRole().toString()
        );
    }
    @Override
    public String toString() {
        return "SignupResponse{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
