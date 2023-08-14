package com.assemble.category.dto.request;

import com.assemble.category.domain.CategoryName;
import com.assemble.category.entity.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryCreationRequest {

    @ApiModelProperty(value = "카테고리 이름")
    @NotEmpty
    private String categoryName;

    public Category toEntity() {
        return new Category(categoryName);
    }
}
