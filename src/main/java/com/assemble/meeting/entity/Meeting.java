package com.assemble.meeting.entity;

import com.assemble.category.entity.Category;
import com.assemble.commons.base.BaseUserEntity;
import com.assemble.comment.domain.Comments;
import com.assemble.commons.converter.BooleanToYNConverter;
import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.file.entity.AttachedFile;
import com.assemble.meeting.domain.Description;
import com.assemble.meeting.domain.MeetingName;
import com.assemble.meeting.domain.MeetingStatus;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@AllArgsConstructor
@SQLDelete(sql = "UPDATE meeting SET is_deleted = 'Y' WHERE meeting_Id = ?")
@Where(clause = "is_deleted = 'N'")
public class Meeting extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId;

    @Embedded
    private MeetingName name;

    @Embedded
    private Description description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ColumnDefault("0")
    private Long hits;

    @ColumnDefault("0")
    private Long likes;

    @ColumnDefault("0")
    private int personnelNumber;

    @Embedded
    private Comments comments;

    @ColumnDefault("0")
    private int expectedPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "meeting")
    private List<MeetingImage> profiles = new ArrayList<>();

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isDeleted;

    @Transient
    private boolean isLike;

    @Enumerated(EnumType.STRING)
    private MeetingStatus meetingStatus;

    protected Meeting() {}

    public Meeting(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Meeting(MeetingName name, Description description, User user, int personnelNumber, int expectedPeriod, Category category) {
        this (null, name, description, user, 0L, 0L, personnelNumber, null, expectedPeriod, category, new ArrayList<>(), false, false, MeetingStatus.PROGRESS);
    }

    public void setProfile(AttachedFile file) {
        this.profiles.add(new MeetingImage(this, this.user, file));
    }

    public void modifyPost(ModifiedMeetingRequest modifiedMeetingRequest) {
        this.name = new MeetingName(modifiedMeetingRequest.getName());
        this.description = new Description(modifiedMeetingRequest.getDescription());
        this.personnelNumber = modifiedMeetingRequest.getPersonnelNumber();
        this.expectedPeriod = modifiedMeetingRequest.getExpectedPeriod();
        this.meetingStatus = MeetingStatus.valueOf(modifiedMeetingRequest.getMeetingStatus());
    }

    public List<ProfileResponse> toPostProfileResponse() {
        return profiles.stream()
                .filter(meetingImage -> meetingImage.getFile() != null)
                .map(meetingImage -> meetingImage.getFile().mapProfile())
                .collect(Collectors.toList());
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public void validateLikeCount() {
        if (this.likes <= 0) {
            throw new IllegalStateException("cannot cancel like");
        }
    }
}