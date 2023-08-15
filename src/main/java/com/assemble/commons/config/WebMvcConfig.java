package com.assemble.commons.config;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.base.BaseRequest;
import com.assemble.commons.interceptor.LoggingCustomInterceptor;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.path}")
    private String basePath;

    private final ObjectMapper objectMapper;

    private final BaseRequest baseRequest;

    private final JwtProvider jwtProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingCustomInterceptor(objectMapper))
                .addPathPatterns("/**").order(2);
        registry.addInterceptor(new TokenInformationInterceptor(baseRequest, jwtProvider))
                .addPathPatterns("/**").order(1);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file://" + basePath + "/")
                .setCachePeriod(20);
    }
}