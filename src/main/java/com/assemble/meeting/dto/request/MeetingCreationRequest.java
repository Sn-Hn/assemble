package com.assemble.meeting.dto.request;

import com.assemble.category.entity.Category;
import com.assemble.meeting.domain.Address;
import com.assemble.meeting.domain.Description;
import com.assemble.meeting.domain.MeetingName;
import com.assemble.meeting.entity.Meeting;
import com.assemble.user.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value = "PostCreationRequest : 모임 작성 요청 값")
@AllArgsConstructor
@Getter
@ToString
public class MeetingCreationRequest {

    @ApiModelProperty(value = "모임 이름", required = true)
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "모임 설명", required = true)
    @NotEmpty
    private String description;

    @ApiModelProperty(value = "모임 카테고리", required = true, example = "1")
    @NotNull
    private Long categoryId;

    @ApiModelProperty(value = "우편 번호", required = true, example = "10011")
    @NotNull
    private String zipCode;

    @ApiModelProperty(value = "도로명 주소", required = true, example = "서울시 강남구")
    @NotNull
    private String roadNameAddress;

    @ApiModelProperty(value = "지번 주소", required = true, example = "서울시 강남구")
    @NotNull
    private String lotNumberAddress;

    @ApiModelProperty(value = "모임 카테고리", required = true, example = "상세 주소")
    private String detailAddress;

    private MeetingCreationRequest() {
    }

    public Meeting toEntity(Long userId) {
        return new Meeting(
                new MeetingName(this.name),
                new Description(this.description),
                new User(userId),
                new Category(categoryId),
                new Address(zipCode, roadNameAddress, lotNumberAddress, detailAddress)
        );
    }
}
