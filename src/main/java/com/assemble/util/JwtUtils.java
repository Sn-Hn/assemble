package com.assemble.util;


import com.assemble.auth.domain.JwtType;
import com.assemble.commons.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class JwtUtils {
    private static final String AUTH = "Authorization";
    private static final String BEARER = "Bearer";
    private static final String BLANK = "";

    public static String getUserIdByRefreshToken(HttpServletRequest request) {
        return getUserIdByTokenFromCookie(request, JwtType.REFRESH_TOKEN);
    }

    public static String getUserIdByTokenFromCookie(HttpServletRequest request, JwtType jwtType) {
        return Arrays.stream(request.getCookies())
                .filter(cookies -> cookies.getName().equals(jwtType.getCode()))
                .findAny()
                .orElseThrow(() -> new NotFoundException(JwtType.class, jwtType))
                .getValue();
    }

    public static String getAccessTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTH);
        return header.replace(BEARER, BLANK).trim();
    }

}
