package com.assemble.file.domain;

import com.assemble.file.entity.AttachedFile;
import com.assemble.file.fixture.FileFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;

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
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> uploadFile.upload(file));
    }

    @Test
    void 파일_업로드_성공() throws IOException {
        // given
        MultipartFile multipartFile = FileFixture.MultipartFile_생성();

        // when
        AttachedFile file = uploadFile.upload(multipartFile);

        // then
        assertThat(file.getName()).isEqualTo(multipartFile.getName());
    }
}