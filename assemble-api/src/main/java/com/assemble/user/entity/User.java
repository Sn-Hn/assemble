package com.assemble.user.entity;

import com.assemble.commons.base.BaseDateEntity;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.file.entity.AttachedFile;
import com.assemble.user.domain.*;
import com.assemble.user.dto.request.ModifiedUserRequest;
import com.assemble.user.dto.request.SignupRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "USERS")
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET status = 'WITHDRAWAL' WHERE user_Id = ?")
//@Where(clause = "status != 'WITHDRAWAL'")
public class User extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Embedded
    private Email email;

    @Embedded
    private Name name;

    @Column(name="NICKNAME")
    private String nickname;

    @Embedded
    private Password password;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private BirthDate birthDate;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Embedded
    private UserImages profiles = new UserImages();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime changedPasswordDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String fcmToken;

    public User() {
    }

    public User(Long userId) {
        this.userId = userId;
    }

    public User(Email email, Name name, String nickname, Password password,
                PhoneNumber phoneNumber, BirthDate birthDate, Gender gender) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.role = UserRole.USER;
        this.status = UserStatus.NORMAL;
        this.changedPasswordDate = LocalDateTime.now();
        this.gender = gender;
    }

    public User(Email email, Password password) {
        this.email = email;
        this.password = password;
    }

    public static User createUser(SignupRequest signupRequest) {
        return new User(
                new Email(signupRequest.getEmail()),
                new Name(signupRequest.getName()),
                signupRequest.getNickname(),
                new Password(signupRequest.getPassword()),
                new PhoneNumber(signupRequest.getPhoneNumber()),
                new BirthDate(signupRequest.getBirthDate()),
                Gender.valueOf(signupRequest.getGender())
        );
    }

    public void setProfile(AttachedFile file) {
        this.profiles.add(new UserImage(this, file));
    }

    public ProfileResponse toProfile() {
        return this.profiles.toProfile();
    }

    public List<ProfileResponse> toProfiles() {
        return this.profiles.toProfiles();
    }

    public void validateWithdrawal() {
        if (this.getStatus().equals(UserStatus.WITHDRAWAL)) {
            throw new NotFoundException("withdrawal user", this.getUserId(), this.getNickname());
        }
    }

    public void modifyInfo(ModifiedUserRequest modifiedUserRequest) {
        this.name = new Name(modifiedUserRequest.getName());
        this.nickname = modifiedUserRequest.getNickname();
        this.phoneNumber = new PhoneNumber(modifiedUserRequest.getPhoneNumber());
        this.birthDate = new BirthDate(modifiedUserRequest.getBirthDate());
    }

    public void changePassword(Password password) {
        this.password = password;
        this.changedPasswordDate = LocalDateTime.now();
    }

    public boolean isAdmin() {
        if (this.role.equals(UserRole.ADMIN)) {
            return true;
        }

        return false;
    }

    public void registerFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
