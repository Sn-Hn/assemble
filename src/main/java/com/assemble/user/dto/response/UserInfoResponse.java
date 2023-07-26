package com.assemble.user.dto.response;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private Long userId;

    private String email;

    private String phoneNumber;

    private String role;

    private String nickname;

    private String name;

    private List<ProfileResponse> profile;

    public UserInfoResponse(User user) {
        this (
                user.getUserId(),
                user.getEmail().getValue(),
                user.getPhoneNumber().getValue(),
                user.getRole().toString(),
                user.getNickname(),
                user.getName().getValue(),
                user.getProfiles().stream()
                        .filter(postImage -> postImage.getFile() != null)
                        .map(profile -> profile.getFile().mapProfile())
                        .collect(Collectors.toList())
        );
    }
}
