package com.assemble.schedule.entity;

import com.assemble.commons.base.BaseUserEntity;
import com.assemble.commons.converter.BooleanToYNConverter;
import com.assemble.meeting.entity.Meeting;
import com.assemble.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@AllArgsConstructor
@SQLDelete(sql = "UPDATE schedule SET is_deleted = 'Y' WHERE id = ?")
@Where(clause = "is_deleted = 'N'")
public class Schedule extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingId")
    private Meeting meeting;

    private LocalDate date;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean isDeleted;

    protected Schedule() {
    }

    public Schedule(String title, String content, User user, Meeting meeting, LocalDate date) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.meeting = meeting;
        this.date = date;
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
