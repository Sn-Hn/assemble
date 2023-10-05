package com.assemble.auth.service;

import com.assemble.auth.domain.Jwt;
import com.assemble.auth.domain.JwtProvider;
import com.assemble.auth.repository.JwtRepository;
import com.assemble.commons.exception.RefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtProvider jwtProvider;

    private final JwtRepository jwtRepository;

    @Transactional
    public String issueAccessToken(Long userId, String email) {
        return jwtProvider.createAccessToken(userId, email);
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
        validateToken(refreshToken);

        return jwtProvider.createAccessToken(
                NumberUtils.parseNumber(jwtProvider.getSubject(refreshToken), Long.class),
                jwtProvider.getEmail(refreshToken)
        );
    }

    private void validateToken(String refreshToken) {
        if (!jwtRepository.findByRefreshToken(refreshToken).isPresent() || !jwtProvider.isValidToken(refreshToken)) {
            throw new RefreshTokenException();
        }
    }

}
