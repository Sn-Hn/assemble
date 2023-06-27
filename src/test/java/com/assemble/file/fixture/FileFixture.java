package com.assemble.file.fixture;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileFixture {
    private final static String defaultProfilePath = "C:/Users/P164960/Downloads/기본이미지.jpg";

    public static File File_생성() {
        return new File(defaultProfilePath);
    }

    public static MultipartFile MultipartFile_생성() throws IOException {
        File file = File_생성();
        return new MockMultipartFile(file.getName(), new FileInputStream(file));
    }
}
