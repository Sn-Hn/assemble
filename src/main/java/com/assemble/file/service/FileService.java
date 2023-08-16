package com.assemble.file.service;

import com.assemble.commons.exception.AssembleException;
import com.assemble.file.domain.UploadFile;
import com.assemble.file.entity.AttachedFile;
import com.assemble.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FileService {

    private final UploadFile uploadFile;

    private final FileRepository fileRepository;

    @Transactional(rollbackFor = AssembleException.class)
    public AttachedFile uploadFile(MultipartFile file, Long creatorId) {
        if (!existFile(file)) {
            return null;
        }

        AttachedFile attachedFile = uploadFile.upload(file);
        attachedFile.createUser(creatorId);
        return fileRepository.save(attachedFile);
    }

    private boolean existFile(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            return true;
        }

        return false;
    }
}
