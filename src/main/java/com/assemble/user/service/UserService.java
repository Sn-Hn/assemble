package com.assemble.user.service;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final VerificationService verificationService;

    private final UserContext userContext;

    @Transactional
    public User signup(SignupRequest signupRequest) {
        verificationService.verifyDuplicationEmail(signupRequest.getEmail());
        verificationService.verifyDuplicationNickname(signupRequest.getNickname());

        signupRequest.encodePassword(passwordEncoder);
        User user = User.createUser(signupRequest);

        User savedUser = userRepository.save(user);

        return savedUser;
    }

    @Transactional(readOnly = true)
    public User findUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));

        return user;
    }

    @Transactional
    public boolean withdrawUser() {
        User user = new User(userContext.getUserId());
        userRepository.delete(user);

        return true;
    }
}
