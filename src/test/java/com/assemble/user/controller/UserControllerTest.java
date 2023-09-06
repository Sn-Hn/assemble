package com.assemble.user.controller;

import com.assemble.commons.TokenFixture;
import com.assemble.commons.config.SecurityConfig;
import com.assemble.commons.config.TaskExecutorConfig;
import com.assemble.commons.config.WebMvcConfig;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.assemble.file.fixture.FileFixture;
import com.assemble.file.service.FileService;
import com.assemble.user.dto.request.ChangePasswordRequest;
import com.assemble.user.dto.request.FindEmailRequest;
import com.assemble.user.dto.request.ModifiedUserRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import com.assemble.user.service.UserService;
import com.assemble.util.MultiValueMapConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestPartDescriptor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@Import({SecurityConfig.class, TaskExecutorConfig.class})
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private FileService fileService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private TokenInformationInterceptor tokenInformationInterceptor;

    @Autowired
    private DelegatingSecurityContextAsyncTaskExecutor delegatingSecurityContextAsyncTaskExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원가입_성공() throws Exception {
        SignupRequest signupRequest = UserFixture.회원가입_정상_신청_회원();
        User user = UserFixture.회원();
        given(userService.signup(any())).willReturn(user);

        ResultActions perform = mockMvc.perform(multipart("/signup")
                        .file(FileFixture.MockMultipartFile_생성())
                        .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .queryParams(MultiValueMapConverter.convert(objectMapper, signupRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.userId").value(user.getUserId()));

        perform.andDo(document("/signup",
                requestParameters(
                        parameterWithName("email").description("이메일"),
                        parameterWithName("name").description("이름"),
                        parameterWithName("nickname").description("닉네임"),
                        parameterWithName("phoneNumber").description("핸드폰 번호"),
                        parameterWithName("password").description("비밀번호"),
                        parameterWithName("birthDate").description("생년월일"),
                        parameterWithName("gender").description("성별")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.userId").description("회원 ID"),
                        fieldWithPath("response.email").description("이메일"),
                        fieldWithPath("response.name").description("이름"),
                        fieldWithPath("response.nickname").description("닉네임"),
                        fieldWithPath("response.phoneNumber").description("핸드폰번호"),
                        fieldWithPath("response.role").description("역할"),
                        fieldWithPath("response.gender").description("성별")
                ))
        );
    }

    @Test
    void 특정_회원_조회() throws Exception {
        Long userId = 1L;
        User user = UserFixture.회원();
        given(userService.findUserInfo(any())).willReturn(user);

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.get("/user/{userId}", userId)
                        .header("Authorization", TokenFixture.AccessToken_생성())
                        .contentType(MediaType.APPLICATION_JSON));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.userId").value(user.getUserId()));

        perform.andDo(document("/user/detail",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                pathParameters(
                        parameterWithName("userId").description("회원 Id")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.userId").description("회원 ID"),
                        fieldWithPath("response.email").description("이메일"),
                        fieldWithPath("response.name").description("이름"),
                        fieldWithPath("response.nickname").description("닉네임"),
                        fieldWithPath("response.phoneNumber").description("핸드폰번호"),
                        fieldWithPath("response.role").description("역할"),
                        fieldWithPath("response.profile").description("프로필 사진"),
                        fieldWithPath("response.birthDate").description("생년월일"),
                        fieldWithPath("response.gender").description("성별")
                ))
        );
    }

    @Test
    void 회원탈퇴_성공() throws Exception {
        given(userService.withdrawUser()).willReturn(true);

        ResultActions perform = mockMvc.perform(delete("/user/withdrawal")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", TokenFixture.AccessToken_생성())
                        .contentType(MediaType.APPLICATION_JSON));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("/user/withdrawal",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("닉네임 중복 여부")
                ))
        );
    }

    @Test
    void 회원정보_수정() throws Exception {
        ModifiedUserRequest modifiedUserRequest = UserFixture.회원정보_수정();
        User user = UserFixture.회원();
        user.modifyInfo(modifiedUserRequest);
        given(userService.modifyUserInfo(any())).willReturn(user);

        ResultActions perform = mockMvc.perform(multipart(HttpMethod.PUT, "/user")
                .header("Authorization", TokenFixture.AccessToken_생성())
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .queryParams(MultiValueMapConverter.convert(objectMapper, modifiedUserRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.userId").value(user.getUserId()));

        perform.andDo(document("/user/update",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestParameters(
                        parameterWithName("name").description("이름"),
                        parameterWithName("nickname").description("닉네임"),
                        parameterWithName("phoneNumber").description("핸드폰 번호"),
                        parameterWithName("birthDate").description("생년월일")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.userId").description("회원 ID"),
                        fieldWithPath("response.email").description("이메일"),
                        fieldWithPath("response.name").description("이름"),
                        fieldWithPath("response.nickname").description("닉네임"),
                        fieldWithPath("response.phoneNumber").description("핸드폰번호"),
                        fieldWithPath("response.role").description("역할"),
                        fieldWithPath("response.profile").description("프로필 사진"),
                        fieldWithPath("response.birthDate").description("생년월일"),
                        fieldWithPath("response.gender").description("성별")
                ))
        );
    }

    @Test
    void 이메일_찾기() throws Exception {
        FindEmailRequest findEmailRequest = UserFixture.이메일_찾기_요청();
        User user = UserFixture.회원();
        given(userService.findEmailByUser(any())).willReturn(user);

        ResultActions perform = mockMvc.perform(get("/user/email")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(MultiValueMapConverter.convert(objectMapper, findEmailRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.name").value(user.getName().getValue()));

        perform.andDo(document("/user/email/find",
                requestParameters(
                        parameterWithName("name").description("이름"),
                        parameterWithName("phoneNumber").description("핸드폰 번호")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.userId").description("회원 ID"),
                        fieldWithPath("response.email").description("이메일"),
                        fieldWithPath("response.name").description("이름")
                ))
        );
    }

    @Test
    void 비밀번호_변경() throws Exception {
        ChangePasswordRequest changePasswordRequest = UserFixture.비밀번호_변경_요청();
        given(userService.changePasswordByUser(any())).willReturn(true);

        ResultActions perform = mockMvc.perform(put("/user/password")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(changePasswordRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("/user/password/change",
                requestFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("password").description("변경할 비밀번호")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("비밀번호 변경 여부")
                ))
        );
    }

    @Test
    void 프로필_이미지_변경() throws Exception {
        MockMultipartFile mockMultipartFile = FileFixture.MockMultipartFile_생성();
        ResultActions perform = mockMvc.perform(multipart(HttpMethod.PUT, "/user/profile")
                .file(mockMultipartFile)
                .header("Authorization", TokenFixture.AccessToken_생성())
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("/user/profile/change",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestParts(
                        partWithName("profileImage").description("프로필 이미지")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("비밀번호 변경 여부")
                ))
        );
    }
}
