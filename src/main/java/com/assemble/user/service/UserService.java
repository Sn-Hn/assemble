package com.assemble.user.service;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.user.domain.*;
import com.assemble.user.dto.request.FindEmailRequest;
import com.assemble.user.dto.request.ModifiedUserRequest;
import com.assemble.user.dto.request.ChangePasswordRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import com.assemble.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final UserContext userContext;
    private final JwtProvider jwtProvider;

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

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findEmailByUsers(FindEmailRequest findEmailRequest) {
        return userRepository.findByNameAndPhoneNumber(
                        new Name(findEmailRequest.getName()),
                        new PhoneNumber(findEmailRequest.getPhoneNumber()),
                        new BirthDate(findEmailRequest.getBirthDate()));
    }

    @Transactional
    public boolean changePasswordByUser(ChangePasswordRequest changePasswordRequest) {
        String email = jwtProvider.getSubject(changePasswordRequest.getToken());
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> new IllegalArgumentException("정보와 일치하는 사용자가 존재하지 않습니다."));

        Password password = new Password(passwordEncoder.encode(changePasswordRequest.getPassword()));

        user.changePassword(password);

        return true;
    }

    @Transactional
    public void removeUserProfile() {
        User user = userRepository.findById(AuthenticationUtils.getUserId())
                .orElseThrow(() -> new NotFoundException(User.class, AuthenticationUtils.getUserId()));

        user.getProfiles().clear();
    }
}
