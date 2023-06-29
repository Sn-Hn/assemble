package com.assemble.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileResponse {

    private String originalName;

    private String fileFullPath;
}
