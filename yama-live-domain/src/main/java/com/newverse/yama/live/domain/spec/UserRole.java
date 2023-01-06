package com.newverse.yama.live.domain.spec;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    PUBLISHER,
    SUBSCRIBER;

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public String getAuthority() {
        return ROLE_PREFIX + name();
    }
}
