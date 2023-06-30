package com.assemble.post.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.response.PostCreationResponse;
import com.assemble.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "게시글 Apis")
@RequestMapping(path = "post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "게시글 작성 (모임 등록)")
    @PostMapping
    public ApiResult<PostCreationResponse> createPost(PostCreationRequest postCreationRequest,
                                                      @RequestPart(required = false) MultipartFile multipartFile) {
        return ApiResult.ok(postService.createPost(postCreationRequest, multipartFile));
    }
}
