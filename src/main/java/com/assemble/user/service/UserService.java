package com.assemble.user.service;

import com.assemble.commons.exception.AssembleException;
import com.assemble.file.entity.AttachedFile;
import com.assemble.file.service.FileService;
import com.assemble.user.domain.Email;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;

    @Transactional(rollbackFor = AssembleException.class)
    public User signup(SignupRequest signupRequest, MultipartFile profileImage) {
        User user = User.createUser(signupRequest, passwordEncoder);

        AttachedFile profile = fileService.uploadFile(profileImage, user.getUserId());
        user.setProfile(profile);

        User savedUser = userRepository.save(user);

        return savedUser;
    }

    @Transactional(readOnly = true)
    public boolean validateDuplicationEmail(String emailRequest) {
        Email email = new Email(emailRequest);
        return !userRepository.findByEmail(email)
                .isPresent();
    }

    @Transactional(readOnly = true)
    public boolean validateDuplicationNickname(String nickname) {
        return !userRepository.findByNickname(nickname)
                .isPresent();
    }
}
