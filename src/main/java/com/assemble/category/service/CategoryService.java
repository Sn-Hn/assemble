package com.assemble.category.service;

import com.assemble.category.dto.request.CategoryCreationRequest;
import com.assemble.category.dto.request.ModifiedCategoryRequest;
import com.assemble.category.entity.Category;
import com.assemble.category.repository.CategoryRepository;
import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.AssembleException;
import com.assemble.commons.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserContext userContext;

    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(rollbackFor = AssembleException.class)
    public Category createCategory(CategoryCreationRequest categoryCreationRequest) {
        Category category = categoryCreationRequest.toEntity();
        return categoryRepository.save(category);
    }

    public Category modifyCategory(ModifiedCategoryRequest modifiedCategoryRequest) {
        Category category = categoryRepository.findById(modifiedCategoryRequest.getId())
                .orElseThrow(() -> new NotFoundException(Category.class, modifiedCategoryRequest.getId()));

        category.modify(modifiedCategoryRequest, userContext.getUserId());

        return category;
    }

    public boolean deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Category.class, categoryId));

        categoryRepository.delete(category);

        return true;
    }
}
