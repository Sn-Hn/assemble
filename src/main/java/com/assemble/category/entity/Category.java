package com.assemble.category.entity;

import com.assemble.user.domain.Name;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AttributeOverride(name="value", column = @Column(name="CATEGORY_NAME"))
    private Name name;

    protected Category() {
    }
}
