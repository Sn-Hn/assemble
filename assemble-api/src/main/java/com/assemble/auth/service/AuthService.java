package com.assemble.auth.service;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.commons.exception.UnauthenticationException;
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

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User login(LoginRequest loginRequest) {
        Email email = new Email(loginRequest.getEmail());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(User.class, email));

        user.validateWithdrawal();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword().getValue())) {
            throw new UnauthenticationException();
        }

        user.registerFcmToken(loginRequest.getFcmToken());

        return user;
    }
}
