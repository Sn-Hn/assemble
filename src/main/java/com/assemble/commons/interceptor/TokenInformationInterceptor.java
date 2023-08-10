package com.assemble.commons.interceptor;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.base.BaseRequest;
import com.assemble.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInformationInterceptor implements HandlerInterceptor {

    private final BaseRequest baseRequest;
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessTokenFromHeader = JwtUtils.getAccessTokenFromHeader(request);
        if (!StringUtils.hasText(accessTokenFromHeader)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        Long userId = NumberUtils.parseNumber(jwtProvider.getUserId(accessTokenFromHeader), Long.class);
        String email = jwtProvider.getEmail(accessTokenFromHeader);
        baseRequest.setUserId(userId);
        baseRequest.setEmail(email);

        log.info("BaseRequest={}", baseRequest);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
