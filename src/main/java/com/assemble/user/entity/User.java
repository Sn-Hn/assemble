package com.assemble.user.entity;

import com.assemble.commons.base.BaseDateEntity;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.file.entity.AttachedFile;
import com.assemble.user.domain.*;
import com.assemble.user.dto.request.ModifiedUserRequest;
import com.assemble.user.dto.request.SignupRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserImage> profiles = new ArrayList<>();

    public User() {
    }

    public User(Long userId) {
        this.userId = userId;
    }

    public User(Email email, Name name, String nickname, Password password, PhoneNumber phoneNumber, BirthDate birthDate) {
        this(null, email, name, nickname, password, phoneNumber, birthDate, UserRole.USER, UserStatus.NORMAL, new ArrayList<>());
    }

    public User(Email email, Password password) {
        this(email, null, null, password, null, null);
    }

    public static User createUser(SignupRequest signupRequest) {
        return new User(
                new Email(signupRequest.getEmail()),
                new Name(signupRequest.getName()),
                signupRequest.getNickname(),
                new Password(signupRequest.getPassword()),
                new PhoneNumber(signupRequest.getPhoneNumber()),
                new BirthDate(signupRequest.getBirthDate())
        );
    }

    public void setProfile(AttachedFile file) {
        this.profiles.add(new UserImage(this, file));
    }

    public ProfileResponse toProfile() {
        return profiles.stream()
                .filter(userImage -> userImage.getFile() != null)
                .map(userImage -> userImage.getFile().mapProfile())
                .findFirst()
                .orElse(null);
    }

    public List<ProfileResponse> toProfiles() {
        return profiles.stream()
                .filter(userImage -> userImage.getFile() != null)
                .map(userImage -> userImage.getFile().mapProfile())
                .collect(Collectors.toList());
    }

    public void verifyWithdrawal() {
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
}
