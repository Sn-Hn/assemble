package com.assemble.auth.service;

import com.assemble.auth.domain.Jwt;
import com.assemble.auth.domain.JwtProvider;
import com.assemble.auth.repository.JwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtProvider jwtProvider;

    private final JwtRepository jwtRepository;

    public String issueAccessToken(String email) {
        return jwtProvider.createAccessToken(email);
    }

    @Transactional
    public String issueRefreshToken(String email) {
        String refreshToken = jwtProvider.createRefreshToken(email);

        Jwt jwt = new Jwt(refreshToken);
        if (jwtRepository.findByRefreshToken(jwt.getRefreshToken()).isPresent()) {
            return jwt.getRefreshToken();
        }
        Jwt savedJwt = jwtRepository.save(jwt);

        return savedJwt.getRefreshToken();
    }

    public String reissueAccessToken(String refreshToken) {
        if (!jwtRepository.findByRefreshToken(refreshToken).isPresent()) {
            throw new IllegalArgumentException("invalid refreshToken");
        }

        return jwtProvider.createAccessToken(jwtProvider.getAccount(refreshToken));
    }

}
