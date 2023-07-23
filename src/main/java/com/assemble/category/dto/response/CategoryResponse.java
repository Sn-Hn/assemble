package com.assemble.category.dto.response;

import com.assemble.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    private Long categoryId;

    private String categoryName;

    public CategoryResponse(Category category) {
        this(category.getId(), category.getName().getValue());
    }
}
