package com.assemble.category.dto.request;

import com.assemble.category.domain.CategoryName;
import com.assemble.category.entity.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryCreationRequest {

    private String categoryName;

    public Category toEntity() {
        return new Category(new CategoryName(categoryName));
    }
}
