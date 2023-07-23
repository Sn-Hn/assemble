package com.assemble.commons.filter;

import com.assemble.commons.exception.UnauthorizedException;
import com.assemble.commons.response.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            log.error("UnauthorizedException={}", e.getMessage(), e);
            setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, UnauthorizedException e) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResult<?> error = ApiResult.error(e, HttpStatus.UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
