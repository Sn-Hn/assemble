package com.assemble.user.dto.response;

import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Long userId;

    private String email;

    private String phoneNumber;

    private String role;

    private String nickname;

    private String name;

    public static LoginResponse from(User user) {
         return new LoginResponse(
                user.getId(),
                user.getEmail().getValue(),
                user.getPhoneNumber().getValue(),
                user.getRole().toString(),
                user.getNickName(),
                user.getName().getValue()
        );
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                ", nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
