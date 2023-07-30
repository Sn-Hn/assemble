package com.assemble.user.controller;

import com.assemble.commons.AccessTokenFixture;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.file.fixture.FileFixture;
import com.assemble.user.dto.request.SignupRequest;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import com.assemble.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
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
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원가입_성공() throws Exception {
        SignupRequest signupRequest = UserFixture.회원가입_정상_신청_회원();
        User user = UserFixture.회원();
        given(userService.signup(any(), any())).willReturn(user);

        ResultActions perform = mockMvc.perform(multipart("/signup")
                        .file(FileFixture.MockMultipartFile_생성())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .content(objectMapper.writeValueAsString(signupRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.userId").value(user.getUserId()));

        perform.andDo(document("/signup",
                requestFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("nickname").description("닉네임"),
                        fieldWithPath("phoneNumber").description("핸드폰 번호"),
                        fieldWithPath("password").description("비밀번호"),
                        fieldWithPath("birthDate").description("생년월일")
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
                        fieldWithPath("response.profile").description("프로필 사진")
                ))
        );
    }

    @Test
    void 특정_회원_조회() throws Exception {
        Long userId = 1L;
        User user = UserFixture.회원();
        given(userService.findUserInfo(any())).willReturn(user);

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.get("/user/{userId}", userId)
                        .header("accessToken", AccessTokenFixture.AccessToken_생성())
                        .contentType(MediaType.APPLICATION_JSON));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.userId").value(user.getUserId()));

        perform.andDo(document("/user/{userId}",
                requestHeaders(
                        headerWithName("accessToken").description("Access Token")
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
                        fieldWithPath("response.profile").description("프로필 사진")
                ))
        );
    }

    @Test
    void 회원탈퇴_성공() throws Exception {
        given(userService.withdrawUser()).willReturn(true);

        ResultActions perform = mockMvc.perform(delete("/user/withdrawal")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("accessToken", AccessTokenFixture.AccessToken_생성())
                        .contentType(MediaType.APPLICATION_JSON));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("/user/withdrawal",
                requestHeaders(
                        headerWithName("accessToken").description("Access Token")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("닉네임 중복 여부")
                ))
        );
    }
}
