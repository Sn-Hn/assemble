package com.assemble.category.controller;

import com.assemble.category.dto.request.CategoryCreationRequest;
import com.assemble.category.dto.request.ModifiedCategoryRequest;
import com.assemble.category.entity.Category;
import com.assemble.category.fixture.CategoryFixture;
import com.assemble.category.service.CategoryService;
import com.assemble.commons.TokenFixture;
import com.assemble.commons.filter.JwtFilter;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@WebMvcTest(controllers = CategoryController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
@AutoConfigureRestDocs
@WithMockUser
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 카테고리_목록_조회() throws Exception {
        given(categoryService.getCategories()).willReturn(List.of(CategoryFixture.카테고리()));

        ResultActions perform = mockMvc.perform(get("/category")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty());

        perform.andDo(document("/category",
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response[0].categoryId").description("카테고리 ID"),
                        fieldWithPath("response[0].categoryName").description("카테고리 이름")
                ))
        );
    }

    @Test
    void 카테고리_등록() throws Exception {
        CategoryCreationRequest categoryCreationRequest = CategoryFixture.카테고리_등록();
        given(categoryService.createCategory(any())).willReturn(CategoryFixture.카테고리());

        ResultActions perform = mockMvc.perform(post("/category")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(categoryCreationRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.categoryName").value(categoryCreationRequest.getCategoryName()));

        perform.andDo(document("/category",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestFields(
                        fieldWithPath("categoryName").description("카테고리 이름")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.categoryId").description("카테고리 ID"),
                        fieldWithPath("response.categoryName").description("카테고리 이름")
                ))
        );
    }

    @Test
    void 카테고리_수정() throws Exception {
        ModifiedCategoryRequest modifiedCategoryRequest = CategoryFixture.카테고리_수정();

        Category category = CategoryFixture.카테고리();
        category.modify(modifiedCategoryRequest);
        given(categoryService.modifyCategory(any())).willReturn(category);

        ResultActions perform = mockMvc.perform(patch("/category")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(modifiedCategoryRequest)));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.categoryName").value(modifiedCategoryRequest.getCategoryName()));

        perform.andDo(document("/category",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                requestFields(
                        fieldWithPath("id").description("카테고리 ID"),
                        fieldWithPath("categoryName").description("카테고리 이름")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response.categoryId").description("카테고리 ID"),
                        fieldWithPath("response.categoryName").description("카테고리 이름")
                ))
        );
    }

    @Test
    void 카테고리_삭제() throws Exception {
        Long categoryId = 1L;
        given(categoryService.deleteCategory(anyLong())).willReturn(true);

        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders.delete("/category/{categoryId}", categoryId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", TokenFixture.AccessToken_생성())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        perform.andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(true));

        perform.andDo(document("/category",
                requestHeaders(
                        headerWithName("Authorization").description("Bearer AccessToken")
                ),
                pathParameters(
                        parameterWithName("categoryId").description("카테고리 ID")
                ),
                responseFields(
                        fieldWithPath("success").description("성공 여부"),
                        fieldWithPath("status").description("상태값"),
                        fieldWithPath("error").description("에러 내용"),
                        fieldWithPath("response").description("카테고리 삭제 성공 여부")
                ))
        );
    }
}