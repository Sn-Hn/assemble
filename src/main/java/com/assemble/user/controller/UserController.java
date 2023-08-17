package com.assemble.user.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.file.service.FileService;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.dto.response.SignupResponse;
import com.assemble.user.dto.response.UserInfoResponse;
import com.assemble.user.entity.User;
import com.assemble.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Api(tags = "회원 APIs")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    @ApiOperation(value = "회원가입")
    @PostMapping(value = "signup")
    public ApiResult<SignupResponse> signup(
            @Valid SignupRequest signupRequest,
            @RequestPart(required = false)MultipartFile profileImage) {

        User user = userService.signup(signupRequest);
        fileService.uploadFile(profileImage, user);
        return ApiResult.ok(SignupResponse.from(user), HttpStatus.CREATED);
    }

    @ApiOperation(value = "회원 정보 조회")
    @GetMapping("user/{userId}")
    public ApiResult<UserInfoResponse> getUserInfo(@PathVariable Long userId) {
        return ApiResult.ok(new UserInfoResponse(userService.findUserInfo(userId)));
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("user/withdrawal")
    public ApiResult<Boolean> withdrawUser() {
        return ApiResult.ok(userService.withdrawUser());
    }
}
