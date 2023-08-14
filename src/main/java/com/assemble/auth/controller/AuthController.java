package com.assemble.auth.controller;

import com.assemble.auth.domain.JwtType;
import com.assemble.auth.dto.response.TokenResponse;
import com.assemble.auth.service.AuthService;
import com.assemble.auth.service.JwtService;
import com.assemble.commons.response.ApiResult;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.auth.dto.response.LoginResponse;
import com.assemble.user.entity.User;
import com.assemble.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Duration;

@Api(tags = "인증 APIs")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    @ApiOperation(value = "회원 로그인")
    @PostMapping("authentication")
    public ApiResult<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        User user = authService.login(loginRequest);

        String accessToken = jwtService.issueAccessToken(user.getUserId(), loginRequest.getEmail());
        String refreshToken = jwtService.issueRefreshToken(user.getUserId(), loginRequest.getEmail());
        ResponseCookie cookie = createCookie(JwtType.REFRESH_TOKEN, refreshToken, (int) Duration.ofDays(14).getSeconds());

        response.addHeader("Set-Cookie", cookie.toString());

        return ApiResult.ok(LoginResponse.from(user, new TokenResponse(accessToken)));
    }

    @ApiOperation(value = "Access Token 재발급")
    @PostMapping("auth/token")
    public ApiResult<TokenResponse> reissueAccessToken(HttpServletRequest request) {
        String refreshToken = JwtUtils.getRefreshToken(request);

        return ApiResult.ok(new TokenResponse(jwtService.reissueAccessToken(refreshToken)));
    }

    @ApiOperation(value = "로그아웃")
    @PostMapping("logout")
    public ApiResult logout(HttpServletResponse response) {
        ResponseCookie cookie = createCookie(JwtType.REFRESH_TOKEN, null, 0);
        response.addHeader("Set-Cookie", cookie.toString());

        return ApiResult.ok();
    }

    private ResponseCookie createCookie(JwtType jwtType, String token, int expireTime) {
        return ResponseCookie.from(jwtType.getCode(), token)
                .httpOnly(true)
                .secure(true)
                .maxAge(expireTime)
                .path("/assemble/")
                .sameSite("None")
                .build();
    }
}
