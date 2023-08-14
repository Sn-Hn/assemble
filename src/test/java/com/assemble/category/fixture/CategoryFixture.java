package com.assemble.category.fixture;

import com.assemble.category.domain.CategoryName;
import com.assemble.category.dto.request.CategoryCreationRequest;
import com.assemble.category.dto.request.ModifiedCategoryRequest;
import com.assemble.category.entity.Category;

public class CategoryFixture {
    private static final Long id = 1L;
    private static final String categoryName = "스터디";
    private static final String modifedCategoryName = "Java 스터디";

    public static CategoryCreationRequest 카테고리_등록() {
        return new CategoryCreationRequest(categoryName);
    }

    public static ModifiedCategoryRequest 카테고리_수정() {
        return new ModifiedCategoryRequest(id, modifedCategoryName);
    }

    public static Category 카테고리() {
        return new Category(id, categoryName);
    }
}
