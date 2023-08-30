package com.assemble.user.dto.response;

import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindEmailResponse {
    private Long userId;

    private String name;

    private String email;

    public FindEmailResponse(User user) {
        this (user.getUserId(), user.getName().getValue(), user.getPhoneNumber().getValue());
    }
}
