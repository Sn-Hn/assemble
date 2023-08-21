package com.assemble.post.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.dto.response.PostsResponse;
import com.assemble.post.service.PostLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "게시판 좋아요 Apis")
@RequiredArgsConstructor
@RestController
public class PostLikeController {

    private final PostLikeService postLikeService;

    @ApiOperation(value = "모임 좋아요")
    @PostMapping("post/like")
    public ApiResult<Boolean> like(@RequestBody @Valid PostLikeRequest postLikeRequest) {
        return ApiResult.ok(postLikeService.likePost(postLikeRequest), HttpStatus.CREATED);
    }

    @ApiOperation(value = "모임 좋아요 취소")
    @DeleteMapping("post/like/{postId}")
    public ApiResult<Boolean> cancelLike(@PathVariable Long postId) {
        return ApiResult.ok(postLikeService.cancelLikePost(postId));
    }


    @ApiOperation(value = "내가 좋아요 한 모임")
    @GetMapping(path = "post/like")
    public ApiResult<Page<PostsResponse>> getPostsByLike(Pageable pageable) {
        return ApiResult.ok(postLikeService.getPostsByLike(pageable)
                .map(PostsResponse::new));
    }
}
