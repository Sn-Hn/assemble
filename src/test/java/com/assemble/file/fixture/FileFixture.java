package com.assemble.file.fixture;

import com.assemble.file.entity.AttachedFile;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileFixture {
    private static final String DEFAULT_PROFILE_PATH = "classpath:defaultProfile.jpg";
    private static final Long fileId = 1L;
    private static final String path = "";
    private static final String fullPath = "";
    private static final String name = "";
    private static final long size = 100L;
    private static final String savedName = "";

    public static File File_생성() throws FileNotFoundException {
        return ResourceUtils.getFile(DEFAULT_PROFILE_PATH);
    }

    public static MultipartFile MultipartFile_생성() throws IOException {
        File file = File_생성();
        return new MockMultipartFile(file.getName(), file.getName(), MediaType.MULTIPART_FORM_DATA_VALUE, new FileInputStream(file));
    }

    public static MockMultipartFile MockMultipartFile_생성() throws IOException {
        File file = File_생성();
        return new MockMultipartFile(file.getName(), file.getName(), MediaType.MULTIPART_FORM_DATA_VALUE, new FileInputStream(file));
    }

    public static AttachedFile 첨부파일_생성() {
        return new AttachedFile(fileId, path, fullPath, name, size, savedName);
    }
}
