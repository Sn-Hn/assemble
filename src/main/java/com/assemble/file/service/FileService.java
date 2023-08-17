package com.assemble.file.service;

import com.assemble.commons.exception.AssembleException;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.file.domain.UploadFile;
import com.assemble.file.entity.AttachedFile;
import com.assemble.file.repository.FileRepository;
import com.assemble.user.entity.User;
import com.assemble.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final UploadFile uploadFile;

    private final FileRepository fileRepository;

    private final UserRepository userRepository;

    @Transactional(rollbackFor = AssembleException.class)
    public void uploadFile(MultipartFile file, Long userId) {
        if (!existFile(file)) {
            return;
        }

        AttachedFile uploadFile = this.uploadFile.upload(file);
        uploadFile.createUser(userId);
        AttachedFile savedFile = fileRepository.save(uploadFile);
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(User.class, userId));
        user.setProfile(savedFile);
        log.info("success file upload");
    }

    private boolean existFile(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            return true;
        }

        return false;
    }
}
