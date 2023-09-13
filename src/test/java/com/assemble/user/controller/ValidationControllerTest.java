package com.assemble.user.controller;

import com.assemble.commons.config.WebMvcConfig;
import com.assemble.commons.filter.JwtFilter;
import com.assemble.commons.interceptor.TokenInformationInterceptor;
import com.assemble.user.dto.request.EmailRequest;
import com.assemble.user.dto.request.FindEmailRequest;
import com.assemble.user.dto.request.NicknameRequest;
import com.assemble.user.dto.request.ValidationUserRequest;
import com.assemble.user.entity.User;
import com.assemble.user.fixture.UserFixture;
import com.assemble.user.service.ValidationService;
import com.assemble.util.MultiValueMapConverter;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@WebMvcTest(controllers = ValidationController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@WithMockUser
public class ValidationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidationService validationService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private TokenInformationInterceptor tokenInformationInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 이메일_중복_아님() throws Exception {
        EmailRequest emailRequest = UserFixture.중복_아닌_이메일();
        given(validationService.isDuplicationEmail(any())).willReturn(false);

        ResultActions perform = mockMvc.perform(get("/email/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .params(MultiValueMapConverter.convert(objectMapper, emailRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(false));

        perform.andDo(document("email/validation",
                requestParameters(
                        parameterWithName("email").description("이메일")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("이메일 중복 여부 (true: 중복, false: 중복 아님)")
                ))
        );
    }

    @Test
    void 닉네임_중복_아님() throws Exception {
        NicknameRequest nicknameRequest = UserFixture.중복_아닌_닉네임();
        given(validationService.isDuplicationNickname(any())).willReturn(false);

        ResultActions perform = mockMvc.perform(get("/nickname/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .params(MultiValueMapConverter.convert(objectMapper, nicknameRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(false));

        perform.andDo(document("nickname/validation",
                requestParameters(
                        parameterWithName("nickname").description("닉네임")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("닉네임 중복 여부 (true: 중복, false: 중복 아님)")
                ))
        );
    }

    @Test
    void 계정_확인() throws Exception {
        ValidationUserRequest validationUserRequest = new ValidationUserRequest("test@test.com", "test", "01000000000", "19951128");
        User user = UserFixture.회원();
        String token = "passwordChangeToken";
        given(validationService.checkUser(any())).willReturn(token);

        ResultActions perform = mockMvc.perform(get("/user/validation")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(MultiValueMapConverter.convert(objectMapper, validationUserRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.token").value(token));

        perform.andDo(document("user/validation",
                requestParameters(
                        parameterWithName("email").description("이메일"),
                        parameterWithName("name").description("이름"),
                        parameterWithName("phoneNumber").description("핸드폰 번호"),
                        parameterWithName("birthDate").description("생년월일")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.token").description("비민번호 변경 토큰")
                ))
        );
    }
}
