package com.assemble.commons.exclusion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExclusionApis {

    @Value("#{${jwt.exclusions}}")
    private Map<String, String> exclusionApis;

    private ExclusionApis() {
    }

    public Map<String, String> getExclusionApis() {
        return exclusionApis;
    }
}
