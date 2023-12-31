package com.assemble.commons.filter;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.exception.NotAdminException;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.commons.exception.UnauthorizedException;
import com.assemble.commons.exclusion.ExclusionApis;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import com.assemble.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(0)
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final ExclusionApis exclusionApis;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = JwtUtils.getAccessTokenFromHeader(request);
        if (!jwtProvider.isValidToken(accessToken)) {
            throw new UnauthorizedException("Unauthorization", "Invalid Access Token !");
        }

        accessActuator(request, accessToken);

        log.info("Request UserId={}, Email={}", jwtProvider.getSubject(accessToken), jwtProvider.getEmail(accessToken));

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        String method = request.getMethod();

        if (servletPath.contains("swagger") ||servletPath.contains("docs")) {
            return true;
        }

        AntPathMatcher matcher = new AntPathMatcher();
        Map<String, String> exclusionApi = this.exclusionApis.getExclusionApis();

        return exclusionApi.keySet().stream()
                .anyMatch(key -> matcher.match(key, servletPath)
                        && exclusionApi.get(key).contains(method.toUpperCase()));
    }

    private void accessActuator(HttpServletRequest request, String accessToken) {
        String servletPath = request.getServletPath();
        if (servletPath.contains("actuator")) {
            checkAdmin(accessToken);
        }
    }

    private void checkAdmin(String accessToken) {
        Long userId = Long.valueOf(jwtProvider.getSubject(accessToken));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));

        if (!user.isAdmin()) {
            throw new NotAdminException();
        }
    }
}
