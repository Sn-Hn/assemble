package com.assemble.file.domain;

import com.assemble.file.fixture.FileFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UploadFile")
@TestPropertySource(locations = "classpath:application-value.yml")
@ExtendWith(SpringExtension.class)
class UploadFileTest {

    private UploadFile uploadFile;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        uploadFile = new UploadFile();
        Class<? extends UploadFile> uploadFileClass = uploadFile.getClass();
        Field field = uploadFileClass.getDeclaredField("basePath");
        field.setAccessible(true);
        field.set(uploadFile, "C:/Users/test/file");
    }

    @Test
    void 파일_업로드_Null_검증() {
        MultipartFile file = null;
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> uploadFile.upload(file));
    }
}