package com.assemble.file.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileResponse {

    @ApiModelProperty(value = "파일 Id")
    private Long fileId;

    @ApiModelProperty(value = "파일 이름")
    private String originalName;

    @ApiModelProperty(value = "파일 경로")
    private String filePath;
}
