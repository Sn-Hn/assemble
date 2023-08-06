package com.assemble.commons.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingCustomInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("======================= [Request] =======================");
        log.info("Request URI={} {}", request.getMethod(), request.getRequestURI());
        log.info("Cookies={}", Arrays.toString(request.getCookies()));
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            final ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            byte[] contentAsByteArray = requestWrapper.getContentAsByteArray();
            log.info("Request={}", objectMapper.readTree(contentAsByteArray));
        }
        log.info("==========================================================");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("======================= [Response] =======================");
        log.info("response contenttype={}", response.getContentType());
        final ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
        log.info("Response={}", objectMapper.readTree(responseWrapper.getContentAsByteArray()));
        log.info("==========================================================");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
