package com.assemble.user.service;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.file.repository.FileRepository;
import com.assemble.user.domain.Email;
import com.assemble.user.domain.Name;
import com.assemble.user.domain.Password;
import com.assemble.user.domain.PhoneNumber;
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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final UserContext userContext;

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
    public User findEmailByUser(FindEmailRequest findEmailRequest) {
        return userRepository.findByNameAndPhoneNumber(new Name(findEmailRequest.getName()), new PhoneNumber(findEmailRequest.getPhoneNumber()))
                .orElseThrow(() -> new IllegalArgumentException("정보와 일치하는 사용자가 존재하지 않습니다."));
    }

    // TODO: 2023-08-29 3차 개발 -> 이메일 인증 추가 -신한
    @Transactional
    public boolean changePasswordByUser(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByEmail(new Email(changePasswordRequest.getEmail()))
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
