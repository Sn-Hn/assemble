package com.assemble.auth.controller;

import com.assemble.auth.service.AuthService;
import com.assemble.commons.response.ApiResult;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.auth.dto.response.LoginResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "회원 로그인")
    @PostMapping("authentication")
    public ApiResult<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ApiResult.ok(authService.login(loginRequest));
    }
}
