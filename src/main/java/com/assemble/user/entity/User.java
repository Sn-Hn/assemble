package com.assemble.user.entity;

import com.assemble.user.domain.*;
import com.assemble.user.dto.request.LoginRequest;
import com.assemble.user.dto.request.SignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name="EMAIL"))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name="USER_NAME"))
    private Name name;

    @Column(name="NICKNAME")
    private String nickName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name="PASSWORD"))
    private Password password;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "PHONE_NUMBER"))
    private PhoneNumber phoneNumber;

    @Enumerated
    private UserRole role;

    public User() {
    }

    public User(Long id, Email email, Name name, String nickName, Password password, PhoneNumber phoneNumber, UserRole role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
    public User(Email email, Name name, String nickName, Password password, PhoneNumber phoneNumber) {
        this(null, email, name, nickName, password, phoneNumber, UserRole.USER);
    }

    public User(Email email, Password password) {
        this(email, null, null, password, null);
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Name getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public Password getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }
}
