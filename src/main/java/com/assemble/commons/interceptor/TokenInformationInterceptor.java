package com.assemble.commons.interceptor;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.base.UserContext;
import com.assemble.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
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
        if (!StringUtils.hasText(accessTokenFromHeader)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        Long userId = NumberUtils.parseNumber(jwtProvider.getUserId(accessTokenFromHeader), Long.class);
        String email = jwtProvider.getEmail(accessTokenFromHeader);
        userContext.setUserId(userId);
        userContext.setEmail(email);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null);
        SecurityContext securityContext = new SecurityContextImpl(authentication);
        SecurityContextHolder.setContext(securityContext);

        log.info("UserContext={}", userContext);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
