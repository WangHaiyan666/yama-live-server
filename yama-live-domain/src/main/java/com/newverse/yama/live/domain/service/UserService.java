package com.newverse.yama.live.domain.service;

import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.respository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @NonNull
    private final UserRepository repository;

    public User getUser(@NonNull String token) {
        return repository.findById(
                // mock decode token to get user id
                Long.valueOf(token.replace("ROLE_", ""))
        ).orElseThrow(() -> new AuthenticationServiceException("User not foud : " + token));
    }
}
