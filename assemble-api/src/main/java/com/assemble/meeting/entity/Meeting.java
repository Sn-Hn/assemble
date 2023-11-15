package com.assemble.meeting.entity;

import com.assemble.activity.domain.Activities;
import com.assemble.category.entity.Category;
import com.assemble.commons.base.BaseUserEntity;
import com.assemble.comment.domain.Comments;
import com.assemble.commons.converter.BooleanToYNConverter;
import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.file.entity.AttachedFile;
import com.assemble.join.domain.JoinRequests;
import com.assemble.meeting.domain.*;
import com.assemble.meeting.dto.request.ModifiedMeetingRequest;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

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
    private Long hits = 0L;

    @ColumnDefault("0")
    private Long likes = 0L;

    @Embedded
    private Comments comments = new Comments();

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Embedded
    private MeetingImages profiles = new MeetingImages();

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isDeleted;

    @Transient
    private boolean isLike;

    @Enumerated(EnumType.STRING)
    private MeetingStatus meetingStatus;

    @Embedded
    private JoinRequests joinRequests = new JoinRequests();

    @Embedded
    private Activities activities = new Activities();

    @Embedded
    private Address address;

    protected Meeting() {}

    public Meeting(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Meeting(MeetingName name, Description description, User user, Category category, Address address) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.category = category;
        this.meetingStatus = MeetingStatus.PROGRESS;
        this.address = address;
    }

    public void setProfile(AttachedFile file) {
        this.profiles.add(new MeetingImage(this, this.user, file));
    }

    public void modifyPost(ModifiedMeetingRequest modifiedMeetingRequest) {
        this.name = new MeetingName(modifiedMeetingRequest.getName());
        this.description = new Description(modifiedMeetingRequest.getDescription());
        this.meetingStatus = MeetingStatus.valueOf(modifiedMeetingRequest.getMeetingStatus());
        this.address = new Address(modifiedMeetingRequest.getAddress());
    }

    public List<ProfileResponse> toMeetingProfileResponse() {
        return profiles.toProfileResponse();
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    public void validateLikeCount() {
        if (this.likes <= 0) {
            throw new IllegalStateException("cannot cancel like");
        }
    }

    public boolean isHost(Long userId) {
        if (this.user.getUserId().equals(userId)) {
            return true;
        }

        return false;
    }

    public void increaseHits() {
        this.hits += 1;
    }
}
