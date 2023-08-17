package com.assemble.file.service;

import com.assemble.file.domain.UploadFile;
import com.assemble.file.fixture.FileFixture;
import com.assemble.file.repository.FileRepository;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;

@DisplayName("FileService")
@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private UploadFile uploadFile;

    @Mock
    private FileRepository fileRepository;

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    void 파일_업로드() throws IOException {
        // given
        MultipartFile mockMultipartFile = FileFixture.MultipartFile_생성();
        User mockUser = UserFixture.회원();
        given(uploadFile.upload(mockMultipartFile)).willReturn(FileFixture.첨부파일_생성());

        // when, then
        assertThatCode(() -> fileService.uploadFile(mockMultipartFile, mockUser))
                .doesNotThrowAnyException();
    }

}
