package com.assemble.activity.domain;

import com.assemble.activity.entity.Activity;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
public class Activities {

    @OneToMany(mappedBy = "meeting")
    private List<Activity> values = new ArrayList<>();

    public void add(Activity activity) {
        this.values.add(activity);
    }
}
