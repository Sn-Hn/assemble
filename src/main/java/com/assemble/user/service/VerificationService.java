package com.assemble.user.service;

import com.assemble.user.domain.Email;
import com.assemble.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VerificationService {

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

    protected void verifyDuplicationEmail(String email) {
        if (isDuplicationEmail(email)) {
            throw new IllegalArgumentException("email duplication");
        }
    }

    protected void verifyDuplicationNickname(String nickname) {
        if (isDuplicationNickname(nickname)) {
            throw new IllegalArgumentException("nickname duplication");
        }
    }
}
