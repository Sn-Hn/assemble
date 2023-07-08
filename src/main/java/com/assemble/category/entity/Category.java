package com.assemble.category.entity;

import com.assemble.category.domain.CategoryName;
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
        category.create(creator);

        return category;
    }
}
