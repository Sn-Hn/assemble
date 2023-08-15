package com.assemble.user.dto.response;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ApiModel(value = "SignupResponse : 회원가입 응답 값")
@Getter
@AllArgsConstructor
@ToString
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
    private ProfileResponse profile;

    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.getUserId(),
                user.getEmail().getValue(),
                user.getName().getValue(),
                user.getNickname(),
                user.getPhoneNumber().getValue(),
                user.getRole().toString(),
                user.toProfile()
        );
    }
}
