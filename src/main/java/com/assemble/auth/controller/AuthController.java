package com.assemble.auth.controller;

import com.assemble.auth.domain.JwtType;
import com.assemble.auth.dto.response.TokenResponse;
import com.assemble.auth.service.AuthService;
import com.assemble.auth.service.JwtService;
import com.assemble.commons.response.ApiResult;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.auth.dto.response.LoginResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    @ApiOperation(value = "회원 로그인")
    @PostMapping("authentication")
    public ApiResult<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = jwtService.issueAccessToken(loginRequest.getEmail());
        String refreshToken = jwtService.issueRefreshToken(loginRequest.getEmail());
        Cookie cookie = createCookie(JwtType.REFRESH_TOKEN, refreshToken, (int) Duration.ofDays(14).getSeconds());
        response.addCookie(cookie);

        return ApiResult.ok(LoginResponse.from(authService.login(loginRequest), new TokenResponse(accessToken)));
    }

    private Cookie createCookie(JwtType jwtType, String token, int expireTime) {
        Cookie cookie = new Cookie(jwtType.getCode(), token);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(expireTime);
        cookie.setPath("/");

        return cookie;
    }
}
