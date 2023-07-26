package com.assemble.post.dto.request;

import com.assemble.category.entity.Category;
import com.assemble.commons.base.BaseRequest;
import com.assemble.post.domain.Contents;
import com.assemble.post.domain.Title;
import com.assemble.post.entity.Post;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ApiModel(value = "PostCreationRequest : 게시글 작성 요청 값")
@AllArgsConstructor
@Getter
@ToString
public class PostCreationRequest {

    @ApiModelProperty(value = "게시글 제목", required = true)
    private String title;

    @ApiModelProperty(value = "게시글 내용", required = true)
    private String contents;

    @ApiModelProperty(value = "게시글 카테고리", required = true, example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "모집 인원", example = "0")
    private int personnelNumber;

    @ApiModelProperty(value = "예상 기간", example = "0")
    private int expectedPeriod;

    private PostCreationRequest() {
    }

    public Post toEntity() {
        return new Post(
                new Title(this.title),
                new Contents(this.contents),
                new User(BaseRequest.getUserId()),
                this.personnelNumber,
                this.expectedPeriod,
                new Category(categoryId)
        );
    }
}
