package com.assemble.user.service;

import com.assemble.commons.exception.NotFoundException;
import com.assemble.user.domain.Email;
import com.assemble.user.dto.request.ValidationUserRequest;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ValidationService {

    private final UserRepository userRepository;

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

    public boolean checkUser(ValidationUserRequest validationUserRequest) {
        userRepository.findByEmail(new Email(validationUserRequest.getEmail()))
                .filter(user -> user.getName().getValue().equals(validationUserRequest.getName()) &&
                        user.getPhoneNumber().getValue().equals(validationUserRequest.getPhoneNumber()) &&
                        user.getBirthDate().getValue().equals(validationUserRequest.getBirthDate()))
                .orElseThrow(() -> new NotFoundException(User.class, validationUserRequest));

        return true;
    }
}
