package com.assemble.user.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.user.dto.request.LoginRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.dto.response.LoginResponse;
import com.assemble.user.dto.response.SignupResponse;
import com.assemble.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "User Apis")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원 로그인")
    @PostMapping("authentication")
    public ApiResult<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ApiResult.ok(LoginResponse.from(userService.login(loginRequest)));
    }

    @ApiOperation(value = "회원가입")
    @PostMapping("signup")
    public ApiResult<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        return ApiResult.ok(SignupResponse.from(userService.signup(signupRequest)));
    }
}
