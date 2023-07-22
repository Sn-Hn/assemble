package com.assemble.post.controller;

import com.assemble.auth.domain.JwtProvider;
import com.assemble.commons.response.ApiResult;
import com.assemble.post.dto.request.ModifiedPostRequest;
import com.assemble.post.dto.request.PostCreationRequest;
import com.assemble.post.dto.request.PostSearchRequest;
import com.assemble.post.dto.response.PostCreationResponse;
import com.assemble.post.dto.response.PostResponse;
import com.assemble.post.dto.response.PostsResponse;
import com.assemble.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "게시글 APIs")
@RequestMapping(path = "post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "모임 등록")
    @PostMapping
    public ApiResult<PostCreationResponse> createPost(@RequestBody PostCreationRequest postCreationRequest) {
        return ApiResult.ok(postService.createPost(postCreationRequest), HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "모임 목록 조회")
    @GetMapping
    public ApiResult<Page<PostsResponse>> getPosts(PostSearchRequest postSearchRequest,
                                                   @PageableDefault(size = 12) Pageable pageable) {
        return ApiResult.ok(postService.getPosts(postSearchRequest, pageable)
                .map(PostsResponse::new));
    }

    @ApiOperation(value = "모임 상세 조회")
    @GetMapping(path = "{postId}")
    public ApiResult<PostResponse> getPost(@PathVariable Long postId) {
        return ApiResult.ok(new PostResponse(postService.getPost(postId)));
    }

    @ApiOperation(value = "모임 수정")
    @PatchMapping
    public ApiResult<PostResponse> modifyPost(ModifiedPostRequest modifiedPostRequest) {
        return ApiResult.ok(new PostResponse(postService.modifyPost(modifiedPostRequest)));
    }

    @ApiOperation(value = "모임 삭제")
    @DeleteMapping(path = "{postId}")
    public ApiResult<Boolean> deletePost(@PathVariable Long postId) {
        return ApiResult.ok(postService.deletePost(postId));
    }
}
