package com.assemble.category.controller;

import com.assemble.category.dto.request.CategoryCreationRequest;
import com.assemble.category.dto.request.ModifiedCategoryRequest;
import com.assemble.category.dto.response.CategoryResponse;
import com.assemble.category.service.CategoryService;
import com.assemble.commons.annotation.AdminCheck;
import com.assemble.commons.response.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "카테고리 Apis")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "category")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 목록 조회")
    @GetMapping
    public ApiResult<List<CategoryResponse>> getCategories() {
        return ApiResult.ok(categoryService.getCategories().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toUnmodifiableList()));
    }

    @AdminCheck
    @ApiOperation(value = "카테고리 목록 조회 (관리자)")
    @GetMapping("admin")
    public ApiResult<List<CategoryResponse>> getCategoriesFromAdmin() {
        return ApiResult.ok(categoryService.getCategories().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toUnmodifiableList()));
    }

    @AdminCheck
    @ApiOperation(value = "카테고리 등록")
    @PostMapping
    public ApiResult<CategoryResponse> createCategory(@RequestBody @Valid CategoryCreationRequest categoryCreationRequest) {
        return ApiResult.ok(new CategoryResponse(categoryService.createCategory(categoryCreationRequest)), HttpStatus.CREATED);
    }

    @AdminCheck
    @ApiOperation(value = "카테고리 수정")
    @PutMapping
    public ApiResult<CategoryResponse> modifyCategory(@RequestBody @Valid ModifiedCategoryRequest modifiedCategoryRequest) {
        return ApiResult.ok(new CategoryResponse(categoryService.modifyCategory(modifiedCategoryRequest)));
    }

    @AdminCheck
    @ApiOperation(value = "카테고리 삭제")
    @DeleteMapping("{categoryId}")
    public ApiResult<Boolean> deleteCategory(@PathVariable Long categoryId) {
        return ApiResult.ok(categoryService.deleteCategory(categoryId));
    }
}
