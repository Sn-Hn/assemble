package com.assemble.user.dto.response;

import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApiModel(value = "LoginResponse : 로그인 응답 값")
@Getter
@AllArgsConstructor
public class LoginResponse {

    @ApiModelProperty(value = "회원 ID")
    private Long userId;

    @ApiModelProperty(value = "회원 이메일")
    private String email;

    @ApiModelProperty(value = "회원 핸드폰 번호")
    private String phoneNumber;

    @ApiModelProperty(value = "회원 역할")
    private String role;

    @ApiModelProperty(value = "회원 닉네임")
    private String nickname;

    @ApiModelProperty(value = "회원 이름")
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
