package com.assemble.comment.controller;

import com.assemble.comment.dto.request.CommentCreationRequest;
import com.assemble.comment.dto.request.ModifiedCommentRequest;
import com.assemble.comment.dto.response.CommentResponse;
import com.assemble.comment.service.CommentService;
import com.assemble.commons.response.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(tags = "댓글 APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "comment")
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 작성")
    @PostMapping
    public ApiResult<CommentResponse> createCommnet(@RequestBody CommentCreationRequest commentCreationRequest) {
        return ApiResult.ok(new CommentResponse(commentService.createComment(commentCreationRequest)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "댓글 수정")
    @PatchMapping
    public ApiResult<CommentResponse> modifyComment(ModifiedCommentRequest modifiedCommentRequest) {
        return ApiResult.ok(new CommentResponse(commentService.modifyComment(modifiedCommentRequest)));
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping(path = "{commentId}")
    public ApiResult<Boolean> deleteComment(@PathVariable Long commentId) {
        return ApiResult.ok(commentService.deleteComment(commentId));
    }

    @ApiOperation(value = "특정 유저 댓글 조회")
    @GetMapping("user/{userId}")
    public ApiResult<Page<CommentResponse>> getCommentsByUser(@PathVariable Long userId, Pageable pageable) {
        return ApiResult.ok(commentService.getCommentsByUser(userId, pageable)
                        .map(CommentResponse::new));
    }

}
