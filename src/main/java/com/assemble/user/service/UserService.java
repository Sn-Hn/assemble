package com.assemble.user.service;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.file.repository.FileRepository;
import com.assemble.user.dto.request.ModifiedUserRequest;
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

    private final ValidationService validationService;

    private final UserContext userContext;

    private final FileRepository fileRepository;

    @Transactional
    public User signup(SignupRequest signupRequest) {
        validationService.validateDuplicationEmail(signupRequest.getEmail());
        validationService.validateDuplicationNickname(signupRequest.getNickname());

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

    @Transactional
    public User modifyUserInfo(ModifiedUserRequest modifiedUserRequest) {
        User user = userRepository.findById(userContext.getUserId())
                .orElseThrow(() -> new NotFoundException(User.class, userContext.getUserId()));

        user.modifyInfo(modifiedUserRequest);
        user.getProfiles().clear();

        return user;
    }
}
