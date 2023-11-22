package com.assemble.file.domain;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.assemble.file.entity.AttachedFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.entity.ContentType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.io.FilenameUtils.getName;

@Slf4j
public class UploadFile {

    private final AmazonS3 amazonS3;

    private final String url;

    private final String bucketName;

    public UploadFile(AmazonS3 amazonS3, String url, String bucketName) {
        this.amazonS3 = amazonS3;
        this.url = url;
        this.bucketName = bucketName;
    }

    public S3Object get(String key) {
        GetObjectRequest request = new GetObjectRequest(bucketName, key);
        return amazonS3.getObject(request);
    }

    public String upload(File file) {
        PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), file);
        return executePut(request);
    }

    public AttachedFile upload(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String defaultExtension = multipartFile.getContentType();
        String fileName = createFileName(defaultExtension, originalFilename);
        String fileFullpath = url + "/" + fileName;
        long size = multipartFile.getSize();
        upload(multipartFile.getInputStream(), multipartFile.getInputStream().available(), fileName, multipartFile.getContentType(), null);
        log.info("file full path={}", fileFullpath);
        return new AttachedFile(url, fileFullpath, originalFilename, size, fileName);
    }

    public String upload(byte[] bytes, String basePath, Map<String, String> metadata) {
        String name = !StringUtils.hasText(basePath) ? UUID.randomUUID().toString() : basePath + "/" + UUID.randomUUID();
        return upload(new ByteArrayInputStream(bytes), bytes.length, name + ".jpeg", "image/jpeg", metadata);
    }

    public String upload(InputStream in, long length, String key, String contentType, Map<String, String> metadata) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(length);
        objectMetadata.setContentType(contentType);
        if (metadata != null && !metadata.isEmpty()) {
            objectMetadata.setUserMetadata(metadata);
        }

        PutObjectRequest request = new PutObjectRequest(bucketName, key, in, objectMetadata);
        return executePut(request);
    }

    public void delete(String url) {
        String key = getName(url);
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, key);
        executeDelete(request);
    }

    private String executePut(PutObjectRequest request) {
        amazonS3.putObject(request.withCannedAcl(CannedAccessControlList.PublicRead));
        StringBuilder sb = new StringBuilder(url);
        if (!url.endsWith("/"))
            sb.append("/");
        sb.append(bucketName);
        sb.append("/");
        sb.append(request.getKey());
        return sb.toString();
    }

    private void executeDelete(DeleteObjectRequest request) {
        amazonS3.deleteObject(request);
    }

    private static String createFileName(String defaultExtension, String originalFileName) {
        String name = UUID.randomUUID().toString();

        return name + "." + getExtension(defaultExtension, originalFileName);
    }

    private static String getExtension(String defaultExtension, String originalFileName) {
        return !StringUtils.hasText(FilenameUtils.getExtension(originalFileName)) ?
                defaultExtension : FilenameUtils.getExtension(originalFileName);
    }
}
