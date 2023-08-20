package com.assemble.file.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Optional;

public class CustomMultipartFile implements MultipartFile {

    private byte[] content;
    private String originalFilename;
    private String contentType;
    private String name;

    private CustomMultipartFile(byte[] content, String originalFilename, String contentType, String name) {
        this.content = content;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.name = name;
    }

    public static Optional<CustomMultipartFile> from(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new CustomMultipartFile(
                multipartFile.getBytes(),
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getName()
        ));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(content);
        }
    }
}
