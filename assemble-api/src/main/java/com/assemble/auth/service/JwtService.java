package com.assemble.auth.service;

import com.assemble.auth.domain.Jwt;
import com.assemble.auth.domain.JwtProvider;
import com.assemble.auth.repository.JwtRedisRepository;
import com.assemble.commons.common.RedisPrefix;
import com.assemble.commons.exception.RefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtProvider jwtProvider;

    private final JwtRedisRepository jwtRedisRepository;

    @Transactional
    public String issueAccessToken(Long userId, String email) {
        return jwtProvider.createAccessToken(userId, email);
    }

    @Transactional
    public String issueRefreshToken(Long userId, String email) {
        String refreshTokenKey = RedisPrefix.REFRESH_TOKEN.getPrefix() + userId;
        Jwt savedJwt = jwtRedisRepository.findById(refreshTokenKey)
                .orElseGet(() -> {
                    String refreshToken = jwtProvider.createRefreshToken(userId, email);
                    Jwt jwt = new Jwt(refreshTokenKey, refreshToken);
                    return jwtRedisRepository.save(jwt);
                });

        return savedJwt.getRefreshToken();
    }

    @Transactional(readOnly = true)
    public String reissueAccessToken(String refreshToken) {
        validateToken(refreshToken);

        return jwtProvider.createAccessToken(
                NumberUtils.parseNumber(jwtProvider.getSubject(refreshToken), Long.class),
                jwtProvider.getEmail(refreshToken)
        );
    }

    @Transactional
    public void removeRefreshToken(String refreshToken) {
        String refreshTokenKey = RedisPrefix.REFRESH_TOKEN.getPrefix() + jwtProvider.getSubject(refreshToken);
        Jwt jwt = jwtRedisRepository.findById(refreshTokenKey)
                .orElseThrow(RefreshTokenException::new);

        jwtRedisRepository.delete(jwt);
    }

    private void validateToken(String refreshToken) {
        String refreshTokenKey = RedisPrefix.REFRESH_TOKEN.getPrefix() + jwtProvider.getSubject(refreshToken);
        if (!jwtRedisRepository.findById(refreshTokenKey).isPresent() || !jwtProvider.isValidToken(refreshToken)) {
            throw new RefreshTokenException();
        }
    }

}
