package com.newverse.yama.live.api.spec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HeaderName {
    public static final String USER_TOKEN = "X-User-Token";
    public static final String CSRF_TOKEN = "X-Csrf-Token";
}
