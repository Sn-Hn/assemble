package com.assemble.file.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.commons.converter.BooleanToYNConverter;
import com.assemble.file.dto.response.ProfileResponse;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "UPDATE attached_file SET is_deleted = 'Y' WHERE file_id = ?")
@Where(clause = "is_deleted = 'N'")
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

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isDeleted;

    protected AttachedFile() {
    }

    public AttachedFile(Long fileId, String path, String fullPath, String name, long size, String savedName) {
        this.fileId = fileId;
        this.path = path;
        this.fullPath = fullPath;
        this.name = name;
        this.size = size;
        this.savedName = savedName;
        this.isDeleted = false;
    }

    public AttachedFile(String path, String fullPath, String name, long size, String savedName) {
        this(null, path, fullPath, name, size, savedName);
    }

    public ProfileResponse mapProfile() {
        return new ProfileResponse(this.fileId, this.name, "/images/" + this.savedName);
    }

    @Override
    public String toString() {
        return "AttachedFile{" +
                "fileId=" + fileId +
                ", path='" + path + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", savedName='" + savedName + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
