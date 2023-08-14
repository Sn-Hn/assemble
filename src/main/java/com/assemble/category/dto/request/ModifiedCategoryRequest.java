package com.assemble.category.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifiedCategoryRequest {

    @ApiModelProperty(value = "카테고리 ID", example = "1")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "카테고리 이름")
    @NotEmpty
    private String categoryName;
}
