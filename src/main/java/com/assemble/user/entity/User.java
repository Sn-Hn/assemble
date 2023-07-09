package com.assemble.user.entity;

import com.assemble.commons.base.BaseDateEntity;
import com.assemble.file.entity.AttachedFile;
import com.assemble.user.domain.*;
import com.assemble.auth.dto.request.LoginRequest;
import com.assemble.user.dto.request.SignupRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "USERS")
@AllArgsConstructor
public class User extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Embedded
    private Email email;

    @Embedded
    private Name name;

    @Column(name="NICKNAME")
    private String nickName;

    @Embedded
    private Password password;

    @Embedded
    private PhoneNumber phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<UserImage> profiles = new ArrayList<>();

    public User() {
    }

    public User(Long userId) {
        this.userId = userId;
    }

    public User(Email email, Name name, String nickName, Password password, PhoneNumber phoneNumber) {
        this(null, email, name, nickName, password, phoneNumber, UserRole.USER, new ArrayList<>());
    }

    public User(Email email, Password password) {
        this(email, null, null, password, null);
    }

    public static User createUser(SignupRequest signupRequest, PasswordEncoder passwordEncoder) {
        return new User(
                new Email(signupRequest.getEmail()),
                new Name(signupRequest.getName()),
                signupRequest.getNickname(),
                new Password(signupRequest.getPassword(), passwordEncoder),
                new PhoneNumber(signupRequest.getPhoneNumber())
        );
    }

    public void login(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        if (!this.password.isComparePassword(loginRequest.getPassword(), passwordEncoder)) {
            throw new IllegalArgumentException("invalid password");
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
}
