package com.assemble.util;


import com.assemble.auth.domain.JwtType;
import com.assemble.commons.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class JwtUtils {

    public static String getAccessToken(HttpServletRequest request) {
        return getTokenAccount(request, JwtType.ACCESS_TOKEN);
    }

    public static String getRefreshToken(HttpServletRequest request) {
        return getTokenAccount(request, JwtType.REFRESH_TOKEN);
    }

    // TODO: 2023-07-09 JWT를 Header에 넣을지, Cookie에 넣을지 토의 -신한
    public static String getTokenAccount(HttpServletRequest request, JwtType jwtType) {
        return Arrays.stream(request.getCookies())
                .filter(cookies -> cookies.getName().equals(jwtType.getCode()))
                .findAny()
                .orElseThrow(() -> new NotFoundException(JwtType.class, jwtType))
                .getValue();
    }
}
