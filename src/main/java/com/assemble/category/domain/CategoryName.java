package com.assemble.category.domain;

import lombok.Getter;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class CategoryName {

    @Column(name="CATEGORY_NAME")
    private String value;

    public CategoryName(String value) {
        verifyEmpty(value);
        this.value = value;
    }

    protected CategoryName() {
    }

    private void verifyEmpty(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("category name empty");
        }
    }
}
