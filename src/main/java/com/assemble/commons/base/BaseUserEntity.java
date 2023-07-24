package com.assemble.commons.base;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseUserEntity extends BaseDateEntity {

    @CreatedBy
    private Long creator;

    @LastModifiedBy
    private Long modifier;

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public void createUser(Long creator) {
        this.creator = creator;
        this.modifier = creator;
    }

    public void modifyUser(Long modifier) {
        this.modifier = modifier;
    }
}
