package com.assemble.user.domain;

import com.assemble.file.dto.response.ProfileResponse;
import com.assemble.user.entity.UserImage;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Embeddable
public class UserImages {

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserImage> values = new ArrayList<>();

    public void add(UserImage userImage) {
        this.values.add(userImage);
    }

    public ProfileResponse toProfile() {
        return this.values.stream()
                .filter(userImage -> userImage.getFile() != null)
                .map(userImage -> userImage.getFile().mapProfile())
                .findFirst()
                .orElse(null);
    }

    public List<ProfileResponse> toProfiles() {
        return this.values.stream()
                .filter(userImage -> userImage.getFile() != null)
                .map(userImage -> userImage.getFile().mapProfile())
                .collect(Collectors.toList());
    }

    public void clear() {
        this.values.clear();
    }
}
