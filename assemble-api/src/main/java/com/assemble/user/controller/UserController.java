package com.assemble.user.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.file.domain.CustomMultipartFile;
import com.assemble.file.service.FileService;
import com.assemble.user.dto.request.*;
import com.assemble.user.dto.response.FindEmailResponse;
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
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Api(tags = "회원 Apis")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final FileService fileService;
    private final DelegatingSecurityContextAsyncTaskExecutor executor;

    @ApiOperation(value = "회원가입")
    @PostMapping(value = "signup")
    public ApiResult<SignupResponse> signup(
            @Valid SignupRequest signupRequest,
            @RequestPart(required = false)MultipartFile profileImage) throws IOException {

        User user = userService.signup(signupRequest);

        AuthenticationUtils.setSecurityContextToUser(user.getUserId());

        Optional<CustomMultipartFile> profile = CustomMultipartFile.from(profileImage);
        CompletableFuture.runAsync(() -> profile.ifPresent(file -> {
                    try {
                        fileService.uploadFile(file, user.getUserId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }), executor)
                .exceptionally(e -> {
                    log.warn("fail file upload!!!");
                    log.warn("FileUploadException={}", e.getMessage(), e);
                    return null;
                });

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
            @Valid ModifiedUserRequest modifiedUserRequest) {

        User user = userService.modifyUserInfo(modifiedUserRequest);

        return ApiResult.ok(new UserInfoResponse(user));
    }

    @ApiOperation(value = "프로필 이미지 변경")
    @PutMapping("user/profile")
    public ApiResult<Boolean> changeUserProfile(@RequestPart(required = false) MultipartFile profileImage) throws IOException {
        userService.removeUserProfile();

        CustomMultipartFile.from(profileImage)
                .ifPresent(file -> {
                    try {
                        fileService.uploadFile(file, AuthenticationUtils.getUserId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        return ApiResult.ok(true);
    }

    @ApiOperation(value = "이메일 찾기")
    @GetMapping("user/email")
    public ApiResult<List<FindEmailResponse>> findEmail(@Valid FindEmailRequest findEmailRequest) {
        return ApiResult.ok(userService.findEmailByUsers(findEmailRequest)
                .stream()
                .map(FindEmailResponse::new)
                .collect(Collectors.toUnmodifiableList()));
    }

    @ApiOperation(value = "비밀번호 변경")
    @PutMapping("user/password")
    public ApiResult<Boolean> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ApiResult.ok(userService.changePasswordByUser(changePasswordRequest));
    }
}
