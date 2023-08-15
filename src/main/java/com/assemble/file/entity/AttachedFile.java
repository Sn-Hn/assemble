package com.assemble.file.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.file.dto.response.ProfileResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@EqualsAndHashCode
@Entity
@Getter
public class AttachedFile extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(name = "FILE_PATH")
    private String path;

    @Column(name = "FILE_FULL_PATH")
    private String fullPath;

    @Column(name = "FILE_NAME")
    private String name;

    @Column(name = "FILE_SIZE")
    private long size;

    @Column(name = "FILE_SAVED_NAME")
    private String savedName;


    protected AttachedFile() {
    }

    private AttachedFile(Long fileId, String path, String fullPath, String name, long size, String savedName) {
        this.fileId = fileId;
        this.path = path;
        this.fullPath = fullPath;
        this.name = name;
        this.size = size;
        this.savedName = savedName;
    }

    public AttachedFile(String path, String fullPath, String name, long size, String savedName) {
        this(null, path, fullPath, name, size, savedName);
    }

    public void setCreatorId(Long creatorId) {
        setCreator(creatorId);
        setModifier(creatorId);
    }

    public void setModifierId(Long modifierId) {
        setModifier(modifierId);
    }

    public ProfileResponse mapProfile() {
        return new ProfileResponse(this.name, "/images/" + this.savedName);
    }
}
