package com.assemble.join.domain;

import com.assemble.join.entity.JoinRequest;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
public class JoinRequests {

    @OneToMany(mappedBy = "meeting")
    private List<JoinRequest> values = new ArrayList<>();

    public void add(JoinRequest joinRequest) {
        this.values.add(joinRequest);
    }

    public boolean isJoinRequestUser(Long userId) {
        return this.values.stream()
                .filter(joinRequest -> joinRequest.isRequestUser(userId))
                .findAny()
                .isPresent();
    }
}
