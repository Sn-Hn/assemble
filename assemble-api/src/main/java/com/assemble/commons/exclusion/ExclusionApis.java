package com.assemble.commons.exclusion;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExclusionApis {
    private Map<String, String> exclusionApis;

    private ExclusionApis() {
        Map<String, String> apis = new HashMap<>();
        apis.put("/images/**", "GET");
        apis.put("/authentication", "POST");
        apis.put("/signup", "POST");
        apis.put("/email/validation", "GET");
        apis.put("/nickname/validation", "GET");
        apis.put("/meeting", "GET");
        apis.put("/meeting/*", "GET");
        apis.put("/category", "GET");
        apis.put("/auth/token", "POST");
        apis.put("/user/validation", "POST");
        apis.put("/user/email", "GET");
        apis.put("/user/password", "PUT");
        apis.put("/notification", "GET");

        this.exclusionApis = apis;
    }

    public Map<String, String> getExclusionApis() {
        return exclusionApis;
    }
}