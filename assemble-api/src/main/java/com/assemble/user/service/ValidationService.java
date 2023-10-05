package com.assemble.user.service;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.user.domain.Email;
import com.assemble.user.dto.request.ValidationPasswordRequest;
import com.assemble.user.dto.request.ValidationUserRequest;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import com.assemble.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ValidationService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean isDuplicationEmail(String emailRequest) {
        Email email = new Email(emailRequest);
        return userRepository.findByEmail(email)
                .isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isDuplicationNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .isPresent();
    }

    protected void validateDuplicationEmail(String email) {
        if (isDuplicationEmail(email)) {
            throw new IllegalArgumentException("email duplication");
        }
    }

    protected void validateDuplicationNickname(String nickname) {
        if (isDuplicationNickname(nickname)) {
            throw new IllegalArgumentException("nickname duplication");
        }
    }

    public String checkUser(ValidationUserRequest validationUserRequest) {
        User normalUser = userRepository.findByEmail(new Email(validationUserRequest.getEmail()))
                .filter(user -> user.getName().getValue().equals(validationUserRequest.getName()) &&
                        user.getPhoneNumber().getValue().equals(validationUserRequest.getPhoneNumber()) &&
                        user.getBirthDate().getValue().equals(validationUserRequest.getBirthDate()))
                .orElseThrow(() -> new NotFoundException(User.class, validationUserRequest));

        return jwtProvider.createChangePasswordToken(normalUser.getEmail().getValue());
    }

    public String checkPassword(ValidationPasswordRequest validationUserRequest) {
        Long userId = AuthenticationUtils.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));

        if (!passwordEncoder.matches(validationUserRequest.getPassword(), user.getPassword().getValue())) {
            throw new IllegalArgumentException("비밀번호를 잘못 입력했습니다.");
        }

        return jwtProvider.createChangePasswordToken(user.getEmail().getValue());
    }
}
