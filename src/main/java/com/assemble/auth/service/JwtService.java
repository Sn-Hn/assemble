package com.assemble.auth.service;

import com.assemble.auth.domain.AccessToken;
import com.assemble.auth.domain.Jwt;
import com.assemble.auth.domain.JwtProvider;
import com.assemble.auth.repository.AccessTokenRedisRepository;
import com.assemble.auth.repository.JwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtProvider jwtProvider;

    private final JwtRepository jwtRepository;

    private final AccessTokenRedisRepository accessTokenRedisRepository;

    @Transactional
    public String issueAccessToken(Long userId, String email) {
        String token = jwtProvider.createAccessToken(userId, email);
        AccessToken accessToken = new AccessToken(jwtProvider.getUserId(token), token, (int) Duration.ofMinutes(30).toMillis());

        AccessToken savedAccessToken = accessTokenRedisRepository.save(accessToken);

        return savedAccessToken.getToken();
    }

    @Transactional
    public String issueRefreshToken(Long userId, String email) {
        String refreshToken = jwtProvider.createRefreshToken(userId, email);

        Jwt jwt = new Jwt(refreshToken);
        if (jwtRepository.findByRefreshToken(jwt.getRefreshToken()).isPresent()) {
            return jwt.getRefreshToken();
        }
        Jwt savedJwt = jwtRepository.save(jwt);

        return savedJwt.getRefreshToken();
    }

    @Transactional(readOnly = true)
    public String reissueAccessToken(String refreshToken) {
        if (!jwtRepository.findByRefreshToken(refreshToken).isPresent() || !jwtProvider.isValidToken(refreshToken)) {
            throw new IllegalArgumentException("invalid refreshToken");
        }

        return jwtProvider.createAccessToken(
                NumberUtils.parseNumber(jwtProvider.getUserId(refreshToken), Long.class),
                jwtProvider.getEmail(refreshToken)
        );
    }

}
