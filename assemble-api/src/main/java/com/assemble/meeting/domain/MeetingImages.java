package com.assemble.meeting.domain;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.meeting.entity.MeetingImage;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Embeddable
public class MeetingImages {

    @OneToMany(mappedBy = "meeting")
    private List<MeetingImage> values = new ArrayList<>();

    public void add(MeetingImage meetingImage) {
        this.values.add(meetingImage);
    }

    public List<ProfileResponse> toProfileResponse() {
        return this.values.stream()
                .filter(meetingImage -> meetingImage.getFile() != null)
                .map(meetingImage -> meetingImage.getFile().mapProfile())
                .collect(Collectors.toList());
    }
}
