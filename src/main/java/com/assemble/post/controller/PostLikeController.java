package com.assemble.post.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.post.dto.request.PostLikeRequest;
import com.assemble.post.service.PostLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "게시판 좋아요 APIs")
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
}
