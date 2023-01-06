package com.newverse.yama.live.api.configuration.security;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.spec.UserRole;
import lombok.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@SuppressWarnings("serial")
public class UserTokenAuthentication extends AbstractAuthenticationToken {
    @NonNull
    private final String userToken;

    private final User user;

    public UserTokenAuthentication(@NonNull String userToken, UserRole role, User user) {
        super(role == null ? emptySet() : singleton(role));
        this.userToken = userToken;
        this.user = user;
        if (role != null) {
            setAuthenticated(true);
        }
    }

    @Override
    public String getCredentials() {
        return userToken;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
