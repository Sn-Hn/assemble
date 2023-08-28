package com.assemble.auth.controller;

import com.assemble.auth.domain.JwtType;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.auth.service.AuthService;
import com.assemble.auth.service.JwtService;
import com.assemble.commons.TokenFixture;
import com.assemble.commons.config.SecurityConfig;
import com.assemble.commons.config.WebMvcConfig;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.assemble.user.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@WebMvcTest(controllers = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@Import(SecurityConfig.class)
@WithMockUser
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private TokenInformationInterceptor tokenInformationInterceptor;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "executor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    void 로그인() throws Exception {
        LoginRequest loginRequest = UserFixture.로그인_시도_회원();
        given(authService.login(any())).willReturn(UserFixture.회원());
        given(jwtService.issueAccessToken(anyLong(), anyString())).willReturn(TokenFixture.AccessToken_생성());
        given(jwtService.issueRefreshToken(anyLong(), anyString())).willReturn(TokenFixture.RefreshToken_생성());

        ResultActions perform = mockMvc.perform(post("/authentication")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.email").value(loginRequest.getEmail()));

        perform.andDo(document("/authentication",
                requestFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("password").description("비밀번호")
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
                        fieldWithPath("response.token.accessToken").description("Access Token")
                ))
        );
    }

    @Test
    void Access_Token_재발급() throws Exception {
        LoginRequest loginRequest = UserFixture.로그인_시도_회원();
        given(jwtService.reissueAccessToken(anyString())).willReturn(TokenFixture.AccessToken_생성());
        Cookie cookie = new Cookie(JwtType.REFRESH_TOKEN.getCode(), TokenFixture.RefreshToken_생성());

        ResultActions perform = mockMvc.perform(post("/auth/token")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
                .cookie(cookie));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("/auth/token",
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.accessToken").description("Access Token")
                ))
        );
    }

    @Test
    void 로그아웃() throws Exception {
        Cookie cookie = new Cookie(JwtType.REFRESH_TOKEN.getCode(), TokenFixture.RefreshToken_생성());
        ResultActions perform = mockMvc.perform(post("/logout")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(cookie));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isEmpty());

        perform.andDo(document("/logout",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("로그아웃 완료 (null)")
                ))
        );
    }
}