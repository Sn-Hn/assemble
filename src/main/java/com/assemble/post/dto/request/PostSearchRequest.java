package com.assemble.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSearchRequest {

    private String searchQuery;

    private String searchBy;

}
