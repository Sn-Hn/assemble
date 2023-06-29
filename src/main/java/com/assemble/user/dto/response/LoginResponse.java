package com.assemble.user.dto.response;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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

    @ApiModelProperty(value = "프로필 사진")
    private List<ProfileResponse> profile;

    public static LoginResponse from(User user) {
        return new LoginResponse(
                user.getUserId(),
                user.getEmail().getValue(),
                user.getPhoneNumber().getValue(),
                user.getRole().toString(),
                user.getNickName(),
                user.getName().getValue(),
                user.getProfiles().stream()
                        .filter(userImage -> userImage.getFile() != null)
                        .map(userImage -> userImage.getFile().mapProfile())
                        .collect(Collectors.toList())
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
