package com.assemble.user.entity;

import com.assemble.commons.base.BaseDateEntity;
import com.assemble.commons.exception.UnauthenticationException;
import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.file.entity.AttachedFile;
import com.assemble.user.domain.*;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.user.dto.request.SignupRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public static User createUser(SignupRequest signupRequest, PasswordEncoder passwordEncoder) {
        return new User(
                new Email(signupRequest.getEmail()),
                new Name(signupRequest.getName()),
                signupRequest.getNickname(),
                new Password(signupRequest.getPassword(), passwordEncoder),
                new PhoneNumber(signupRequest.getPhoneNumber()),
                new BirthDate(signupRequest.getBirthDate())
        );
    }

    public void login(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        if (!this.password.isComparePassword(loginRequest.getPassword(), passwordEncoder)) {
            throw new UnauthenticationException();
        }
    }

    public void setProfile(AttachedFile file) {
        this.profiles.add(new UserImage(this, file));
    }

    public List<String> getUserImages() {
        return getProfiles().stream()
                .map(userImage -> userImage.getFile().getFullPath())
                .collect(Collectors.toList());
    }

    public List<ProfileResponse> toUserProfileResponse() {
        return profiles.stream()
                .filter(userImage -> userImage.getFile() != null)
                .map(userImage -> userImage.getFile().mapProfile())
                .collect(Collectors.toList());
    }
}
