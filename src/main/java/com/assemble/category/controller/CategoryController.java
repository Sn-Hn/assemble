package com.assemble.category.controller;

import com.assemble.category.dto.response.CategoryResponse;
import com.assemble.category.service.CategoryService;
import com.assemble.commons.response.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "카테고리 APIs")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 목록 조회")
    @GetMapping("category")
    public ApiResult<List<CategoryResponse>> getCategories() {
        return ApiResult.ok(categoryService.getCategories().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toUnmodifiableList()));
    }
}
