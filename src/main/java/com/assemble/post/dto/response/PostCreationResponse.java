package com.assemble.post.dto.response;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.post.entity.Post;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel(value = "PostCreationResponse : 게시글 작성 응답 값")
@AllArgsConstructor
@Getter
@ToString
public class PostCreationResponse {

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String contents;

    @ApiModelProperty(value = "게시글 카테고리")
    private Long categoryId;

    @ApiModelProperty(value = "작성자 닉네임")
    private String writerNickname;

    @ApiModelProperty(value = "작성자 ID")
    private Long writerId;

    @ApiModelProperty(value = "게시글 조회수")
    private Long hits;

    @ApiModelProperty(value = "게시글 좋아요 수")
    private Long likeCount;

    @ApiModelProperty(value = "게시글 프로필 사진 목록")
    private List<ProfileResponse> postProfile;

    public PostCreationResponse(Post post) {
        this(
                post.getTitle().getValue(),
                post.getContents().getValue(),
                post.getCategory().getId(),
                post.getUser().getNickName(),
                post.getUser().getUserId(),
                post.getHits(),
                0L,
                post.getProfiles().stream()
                        .filter(postImage -> postImage.getFile() != null)
                        .map(profile -> profile.getFile().mapProfile())
                        .collect(Collectors.toList())
        );
    }
}
