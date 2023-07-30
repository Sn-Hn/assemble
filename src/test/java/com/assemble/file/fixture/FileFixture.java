package com.assemble.file.fixture;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileFixture {
    private final static String defaultProfilePath = "classpath:defaultProfile.jpg";

    public static File File_생성() throws FileNotFoundException {
        return ResourceUtils.getFile("classpath:defaultProfile.jpg");
    }

    public static MultipartFile MultipartFile_생성() throws IOException {
        File file = File_생성();
        return new MockMultipartFile(file.getName(), file.getName(), MediaType.MULTIPART_FORM_DATA_VALUE, new FileInputStream(file));
    }

    public static MockMultipartFile MockMultipartFile_생성() throws IOException {
        File file = File_생성();
        return new MockMultipartFile(file.getName(), file.getName(), MediaType.MULTIPART_FORM_DATA_VALUE, new FileInputStream(file));
    }
}
