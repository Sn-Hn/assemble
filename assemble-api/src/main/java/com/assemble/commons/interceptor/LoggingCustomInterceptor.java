package com.assemble.commons.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingCustomInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        printRequestLog(request);
        printResponseLog(response);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private void printRequestLog(HttpServletRequest request) throws IOException {
        log.info("======================= [Request] =======================");
        log.info("Request Method={}, URI={}", request.getMethod(), request.getRequestURI());
        log.info("Remote Request User={}, ADDR={}, HOST={}, PORT={}", request.getRemoteUser(), request.getRemoteAddr(), request.getRemoteHost(), request.getRemotePort());

        final Cookie[] cookies = request.getCookies();
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            log.info("Cookie Name={}, Value={}", cookies[i].getName(), cookies[i].getValue());
        }

        log.info("Request Param={}", request.getParameterMap().keySet());

        if (request instanceof ContentCachingRequestWrapper) {
            final ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            log.info("Request={}", objectMapper.readTree(requestWrapper.getContentAsByteArray()));
        }
        log.info("==========================================================");
    }

    private void printResponseLog(HttpServletResponse response) throws Exception {
        log.info("======================= [Response] =======================");
        log.info("response contentType={}", response.getContentType());
        if (response instanceof ContentCachingResponseWrapper) {
            final ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            log.info("Response={}", objectMapper.readTree(responseWrapper.getContentAsByteArray()));
        }
        log.info("==========================================================");
    }
}
