package com.assemble.user.service;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.user.domain.Email;
import com.assemble.user.dto.request.LoginRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User login(LoginRequest loginRequest) {
        Email email = new Email(loginRequest.getEmail());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(User.class, email));

        user.login(loginRequest, passwordEncoder);

        return user;
    }

    public User signup(SignupRequest signupRequest) {
        User user = User.createUser(signupRequest, passwordEncoder);
        User savedUser = userRepository.save(user);

        return savedUser;
    }
}
