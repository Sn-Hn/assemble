package com.assemble.category.service;

import com.assemble.category.dto.request.CategoryCreationRequest;
import com.assemble.category.dto.request.ModifiedCategoryRequest;
import com.assemble.category.entity.Category;
import com.assemble.category.fixture.CategoryFixture;
import com.assemble.category.repository.CategoryRepository;
import com.assemble.commons.base.BaseRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("CategoryService")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BaseRequest baseRequest;

    @Test
    void 카테고리_등록() {
        // given
        CategoryCreationRequest categoryCreationRequest = CategoryFixture.카테고리_등록();
        given(categoryRepository.save(any())).willReturn(CategoryFixture.카테고리());

        // when
        Category savedCategory = categoryService.createCategory(categoryCreationRequest);

        // then
        assertThat(savedCategory.getName().getValue()).isEqualTo(categoryCreationRequest.getCategoryName());
    }

    @Test
    void 카테고리_조회() {
        // given
        given(categoryRepository.findAll()).willReturn(List.of(CategoryFixture.카테고리()));

        // when
        List<Category> categories = categoryService.getCategories();

        // then
        assertAll(
                () -> assertThat(categories).isNotEmpty(),
                () -> assertThat(categories.size()).isGreaterThanOrEqualTo(1)
        );
    }

    @Test
    void 카테고리_수정() {
        // given
        ModifiedCategoryRequest modifiedCategoryRequest = CategoryFixture.카테고리_수정();
        given(categoryRepository.findById(any())).willReturn(Optional.of(CategoryFixture.카테고리()));
        given(baseRequest.getUserId()).willReturn(1L);

        // when
        Category modifiedCategory = categoryService.modifyCategory(modifiedCategoryRequest);

        // then
        assertThat(modifiedCategory.getName().getValue()).isEqualTo(modifiedCategoryRequest.getCategoryName());
    }

    @Test
    void 카테고리_삭제() {
        // given
        Long id = 2L;
        given(categoryRepository.findById(id)).willReturn(Optional.of(CategoryFixture.카테고리())).willReturn(null);

        // when
        boolean isDelete = categoryService.deleteCategory(id);

        assertThat(isDelete).isTrue();
    }
}