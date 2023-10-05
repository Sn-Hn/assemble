package com.assemble.commons.interceptor;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.base.UserContext;
import com.assemble.util.AuthenticationUtils;
import com.assemble.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final UserContext userContext;
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessTokenFromHeader = JwtUtils.getAccessTokenFromHeader(request);
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        if (!StringUtils.hasText(accessTokenFromHeader)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        Long userId = NumberUtils.parseNumber(jwtProvider.getSubject(accessTokenFromHeader), Long.class);
        String email = jwtProvider.getEmail(accessTokenFromHeader);
        userContext.setUserId(userId);
        userContext.setEmail(email);

        AuthenticationUtils.setSecurityContextToUser(userId);


        log.info("UserContext={}", userContext);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
