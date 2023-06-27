package com.assemble.user.dto.response;

import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApiModel(value = "SignupResponse : 회원가입 응답 값")
@Getter
@AllArgsConstructor
public class SignupResponse {
    @ApiModelProperty(value = "회원 ID")
    private Long userId;

    @ApiModelProperty(value = "회원 이메일")
    private String email;

    @ApiModelProperty(value = "회원 이름")
    private String name;

    @ApiModelProperty(value = "회원 닉네임")
    private String nickname;

    @ApiModelProperty(value = "회원 핸드폰 번호")
    private String phoneNumber;

    @ApiModelProperty(value = "회원 역할")
    private String role;

    @ApiModelProperty(value = "프로필 사진")
    private String profilePath;

    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.getId(),
                user.getEmail().getValue(),
                user.getName().getValue(),
                user.getNickName(),
                user.getPhoneNumber().getValue(),
                user.getRole().toString(),
                user.getProfile() != null ? user.getProfile().getPath() + "/" + user.getProfile().getName() : null
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
