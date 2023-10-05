package com.assemble.user.dto.response;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    @ApiModelProperty(value = "회원 ID")
    private Long userId;

    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "핸드폰 번호")
    private String phoneNumber;

    @ApiModelProperty(value = "역할")
    private String role;

    @ApiModelProperty(value = "닉네임")
    private String nickname;

    @ApiModelProperty(value = "이름")
    private String name;

    @ApiModelProperty(value = "생년월일")
    private String birthDate;

    @ApiModelProperty(value = "프로필 이미지")
    private ProfileResponse profile;

    @ApiModelProperty(value = "성별")
    private String gender;

    public UserInfoResponse(User user) {
        this (
                user.getUserId(),
                user.getEmail().getValue(),
                user.getPhoneNumber().getValue(),
                user.getRole().toString(),
                user.getNickname(),
                user.getName().getValue(),
                user.getBirthDate().getValue(),
                user.toProfile(),
                user.getGender().toString()
        );
    }
}
