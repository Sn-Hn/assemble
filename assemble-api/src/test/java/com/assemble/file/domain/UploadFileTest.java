package com.assemble.file.domain;

import com.assemble.annotation.TestCustomProperty;
import com.assemble.file.entity.AttachedFile;
import com.assemble.file.fixture.FileFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UploadFile")
@TestCustomProperty
@SpringBootTest
class UploadFileTest {

    @Autowired
    private UploadFile uploadFile;

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
        assertThat(file.getName()).isEqualTo(multipartFile.getOriginalFilename());
    }

}