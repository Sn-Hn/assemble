package com.assemble.file.domain;

import com.assemble.file.entity.AttachedFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class UploadFile {

    @Value("${file.path}")
    private String basePath;

    public AttachedFile upload(MultipartFile multipartFile) {
        verifyEmptyFile(multipartFile);
        createDirectory();
        try {
            String originalFilename = new String(multipartFile.getOriginalFilename().getBytes("8859_1"), StandardCharsets.UTF_8);
            String defaultExtension = multipartFile.getContentType();
            String fileName = createFileName(defaultExtension, originalFilename);
            long size = multipartFile.getSize();
            multipartFile.transferTo(new File(fileName));
            return new AttachedFile(basePath, basePath + "/" + fileName, originalFilename, size, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
