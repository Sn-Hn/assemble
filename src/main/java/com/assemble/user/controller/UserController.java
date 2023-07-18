package com.assemble.user.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.user.dto.request.EmailRequest;
import com.assemble.user.dto.request.NicknameRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.dto.response.SignupResponse;
import com.assemble.user.dto.response.UserInfoResponse;
import com.assemble.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "회원 APIs")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping(value = "signup")
    public ApiResult<SignupResponse> signup(
            SignupRequest signupRequest,
            @RequestPart(required = false)MultipartFile profileImage) {
        return ApiResult.ok(SignupResponse.from(userService.signup(signupRequest, profileImage)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "이메일 검증")
    @GetMapping("email/validation")
    public ApiResult<Boolean> validateUserEmail(EmailRequest emailRequest) {
        return ApiResult.ok(userService.isDuplicationEmail(emailRequest.getEmail()));
    }

    @ApiOperation(value = "닉네임 검증")
    @GetMapping("nickname/validation")
    public ApiResult<Boolean> validateUserEmail(NicknameRequest nicknameRequest) {
        return ApiResult.ok(userService.isDuplicationNickname(nicknameRequest.getNickname()));
    }

    @ApiOperation(value = "회원 정보 조회")
    @GetMapping("user/{userId}")
    public ApiResult<UserInfoResponse> getUserInfo(@PathVariable Long userId) {
        return ApiResult.ok(new UserInfoResponse(userService.findUserInfo(userId)));
    }

}
