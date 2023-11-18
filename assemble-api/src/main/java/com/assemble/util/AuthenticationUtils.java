package com.assemble.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.List;

public class AuthenticationUtils {

    public static void setSecurityContextToUser(Long userId) {
        setSecurityContext(userId, "USER");
    }

    public static void setSecurityContextToAdmin(Long userId) {
        setSecurityContext(userId, "ADMIN");
    }

    private static void setSecurityContext(Long userId, String role) {
        SimpleGrantedAuthority user = new SimpleGrantedAuthority(role);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(userId, null, List.of(user));
        SecurityContext securityContext = new SecurityContextImpl(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return -1L;
        }

        if (authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }

        return -1L;
    }
}
