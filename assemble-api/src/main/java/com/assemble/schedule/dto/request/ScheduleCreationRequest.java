package com.assemble.schedule.dto.request;

import com.assemble.schedule.entity.Schedule;
import com.assemble.user.entity.User;
import com.assemble.util.AuthenticationUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleCreationRequest {

    @ApiModelProperty(value = "Schedule 제목")
    private String title;

    @ApiModelProperty(value = "Schedule 내용")
    private String content;

    @ApiModelProperty(value = "날짜")
    private LocalDate date;


    public Schedule toEntity() {
        return new Schedule(this.title, this.content, new User(AuthenticationUtils.getUserId()), date);
    }
}
