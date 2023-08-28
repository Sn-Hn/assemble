package com.assemble.user.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.file.domain.CustomMultipartFile;
import com.assemble.file.service.FileService;
import com.assemble.user.dto.request.ModifiedUserRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.dto.response.SignupResponse;
import com.assemble.user.dto.response.UserInfoResponse;
import com.assemble.user.entity.User;
import com.assemble.user.service.UserService;
import com.assemble.util.AuthenticationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Api(tags = "회원 Apis")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    @ApiOperation(value = "회원가입")
    @PostMapping(value = "signup")
    public ApiResult<SignupResponse> signup(
            @Valid SignupRequest signupRequest,
            @RequestPart(required = false)MultipartFile profileImage) throws IOException {

        User user = userService.signup(signupRequest);

        AuthenticationUtils.setSecurityContextToUser(user.getUserId());

        CustomMultipartFile.from(profileImage)
                .ifPresent(file -> fileService.uploadFile(file, user.getUserId()));
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

    @ApiOperation(value = "회원 정보 수정")
    @PutMapping("user")
    public ApiResult<UserInfoResponse> modifyUser(
            @Valid ModifiedUserRequest modifiedUserRequest,
            @RequestPart(required = false)MultipartFile profileImage) throws IOException {

        User user = userService.modifyUserInfo(modifiedUserRequest);
        CustomMultipartFile.from(profileImage)
                .ifPresent(file -> fileService.uploadFile(file, user.getUserId()));
        return ApiResult.ok(new UserInfoResponse(user));
    }
}
