package com.assemble.commons.common;

public enum RedisPrefix {
    REFRESH_TOKEN("RT_");

    private String prefix;

    RedisPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
