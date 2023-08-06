package com.assemble.util;

import com.assemble.auth.domain.JwtType;
import com.assemble.commons.exception.NotFoundException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class JwtUtils {
    private static final String AUTH = "Authorization";
    private static final String BEARER = "Bearer";
    private static final String BLANK = "";

    public static String getRefreshToken(HttpServletRequest request) {
        return getRefreshTokenFromCookie(request, JwtType.REFRESH_TOKEN);
    }

    private static String getRefreshTokenFromCookie(HttpServletRequest request, JwtType jwtType) {
        return Arrays.stream(request.getCookies())
                .filter(cookies -> cookies.getName().equals(jwtType.getCode()))
                .findAny()
                .orElseThrow(() -> new NotFoundException(JwtType.REFRESH_TOKEN.getCode(), jwtType))
                .getValue();
    }

    public static String getAccessTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTH);
        if (!StringUtils.hasText(header)) {
            return null;
        }
        return header.replace(BEARER, BLANK).trim();
    }

}
