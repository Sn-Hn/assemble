package com.assemble.meeting.entity;

import com.assemble.file.entity.AttachedFile;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Entity
public class MeetingImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fileId")
    private AttachedFile file;

    public MeetingImage(Meeting meeting, User user, AttachedFile file) {
        this(null, meeting, user, file);
    }

    protected MeetingImage() {
    }
}
