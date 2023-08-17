package com.assemble.file.domain;

import com.assemble.file.entity.AttachedFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class UploadFile {

    @Value("${file.path}")
    private String basePath;

    public AttachedFile upload(MultipartFile multipartFile) {
        verifyEmptyFile(multipartFile);
        createDirectory();
        log.info("file path={}", basePath);
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String defaultExtension = multipartFile.getContentType();
            String fileName = createFileName(defaultExtension, originalFilename);
            String fileFullpath = basePath + "/" + fileName;
            long size = multipartFile.getSize();
            log.info("file full path={}", fileFullpath);
            multipartFile.transferTo(new File(fileFullpath));
            return new AttachedFile(basePath, fileFullpath, originalFilename, size, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
            return true;
        }

        return false;
    }

    private static String createFileName(String defaultExtension, String originalFileName) {
        String name = UUID.randomUUID().toString();

        return name + "." + getExtension(defaultExtension, originalFileName);
    }

    private static String getExtension(String defaultExtension, String originalFileName) {
        return !StringUtils.hasText(FilenameUtils.getExtension(originalFileName)) ?
                defaultExtension : FilenameUtils.getExtension(originalFileName);
    }

    private void createDirectory() {
        File file = new File(basePath);

        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void verifyEmptyFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("empty file");
        }
    }
}
