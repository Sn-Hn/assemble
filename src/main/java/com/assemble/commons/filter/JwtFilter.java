package com.assemble.commons.filter;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.exclusion.ExclusionApis;
import com.assemble.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final ExclusionApis exclusionApis;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String api = request.getRequestURI();
        String method = request.getMethod();

        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);

//        if (!excludeValidationApi(exclusionApis.getExclusionApis(), api, method) && !jwtProvider.validateToken(JwtUtils.getAccessToken(request))) {
//            throw new IllegalArgumentException("expired access token");
//        }

        filterChain.doFilter(wrappingRequest, wrappingResponse);
        wrappingResponse.copyBodyToResponse();
    }

    private boolean excludeValidationApi(Map<String, String> exclusionApis, String api, String method) {
        if (exclusionApis.containsKey(api) && exclusionApis.get(api).contains(method)) {
            return true;
        }

        return false;
    }
}
