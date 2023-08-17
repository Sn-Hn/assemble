package com.assemble.file.service;

import com.assemble.commons.exception.AssembleException;
import com.assemble.commons.exception.FileUploadException;
import com.assemble.file.domain.UploadFile;
import com.assemble.file.entity.AttachedFile;
import com.assemble.file.repository.FileRepository;
import com.assemble.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final UploadFile uploadFile;

    private final FileRepository fileRepository;

    @Transactional(rollbackFor = AssembleException.class)
    public void uploadFile(MultipartFile file, User user) {
        if (!existFile(file)) {
            return;
        }

        CompletableFuture.supplyAsync(() -> uploadFile.upload(file)).thenAccept(f -> {
            f.createUser(user.getUserId());
            AttachedFile savedFile = fileRepository.save(f);
            user.setProfile(savedFile);
            log.info("success file upload");
        }).exceptionally(e -> {
            log.warn("fail file upload!!!");
            log.warn("FileUploadException={}", e.getMessage(), e);
            throw new FileUploadException();
        });
    }

    private boolean existFile(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            return true;
        }

        return false;
    }
}
