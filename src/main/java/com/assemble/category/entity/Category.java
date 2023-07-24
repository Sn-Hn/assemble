package com.assemble.category.entity;

import com.assemble.category.domain.CategoryName;
import com.assemble.category.dto.request.ModifiedCategoryRequest;
import com.assemble.commons.base.BaseRequest;
import com.assemble.commons.base.BaseUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Entity
public class Category extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="CATEGORY_NAME")
    private CategoryName name;

    protected Category() {
    }

    public Category(Long id) {
        this(id, null);
    }

    public Category(CategoryName name) {
        this (null, name);
    }

    public static Category createCategory(String name, Long creator) {
        Category category = new Category(new CategoryName(name));
        category.createUser(creator);

        return category;
    }

    public void modify(ModifiedCategoryRequest modifiedCategoryRequest) {
        this.name = new CategoryName(modifiedCategoryRequest.getCategoryName());
        modifyUser(BaseRequest.getUserId());
    }
}
