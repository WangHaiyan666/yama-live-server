package com.newverse.yama.live.api.configuration.security;

import com.newverse.yama.live.domain.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
@RequiredArgsConstructor
public class UserTokenAuthenticationProvider implements AuthenticationProvider {
    @NonNull
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticate((UserTokenAuthentication) authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UserTokenAuthentication.class.isAssignableFrom(authentication);
    }

    private Authentication authenticate(UserTokenAuthentication authentication) {
        val user = userService.getUser(authentication.getCredentials());

        return new UserTokenAuthentication(authentication.getCredentials(),
                                           user.getRole(),
                                           user);
    }
}
