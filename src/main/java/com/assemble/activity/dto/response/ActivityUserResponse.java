package com.assemble.activity.dto.response;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.activity.entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityUserResponse {

    private Long assembleId;

    private Long userId;

    private String nickname;

    private ProfileResponse profile;

    private boolean isHost;

    public ActivityUserResponse(Activity activity) {
        this(
                activity.getPost().getPostId(),
                activity.getUser().getUserId(),
                activity.getUser().getNickname(),
                activity.getUser().toProfile(),
                activity.isHost()
        );
    }
}
