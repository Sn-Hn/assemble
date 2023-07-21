package com.assemble.commons.exclusion;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExclusionApis {
    private Map<String, String> exclusionApis;

    private ExclusionApis() {
        Map<String, String> apis = new HashMap<>();
        apis.put("/assemble/authentication", "POST");
        apis.put("/assemble/signup", "POST");
        apis.put("/assemble/email/validation", "GET");
        apis.put("/assemble/nickname/validation", "GET");
        apis.put("/assemble/post", "GET");
        apis.put("/assemble/post/{postId}", "GET");

        this.exclusionApis = apis;
    }

    public Map<String, String> getExclusionApis() {
        return exclusionApis;
    }
}
