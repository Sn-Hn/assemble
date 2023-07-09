package com.assemble.auth.service;

import com.assemble.auth.dto.response.LoginResponse;
import com.assemble.auth.dto.response.TokenResponse;
import com.assemble.commons.exception.AssembleException;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.user.domain.Email;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = AssembleException.class)
    public LoginResponse login(LoginRequest loginRequest) {
        Email email = new Email(loginRequest.getEmail());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(User.class, email));

        user.login(loginRequest, passwordEncoder);

        String accessToken = jwtService.issueAccessToken(loginRequest.getEmail());
        String refreshToken = jwtService.issueRefreshToken(loginRequest.getEmail());

        LoginResponse response = LoginResponse.from(user, new TokenResponse(accessToken, refreshToken));

        return response;
    }
}
