package com.assemble.file.dto.response;

import com.assemble.file.entity.AttachedFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileResponse {

    @ApiModelProperty(value = "파일 경로")
    private String fileFullPath;

    @ApiModelProperty(value = "파일 이름")
    private String originalName;

    public FileResponse(AttachedFile file) {
        this(file.getFullPath(), file.getName());
    }
}
