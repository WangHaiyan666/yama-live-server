package com.newverse.yama.live.infrastructure.repository;

import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.respository.UserRepository;
import com.newverse.yama.live.infrastructure.mapper.UserMapper;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    @NonNull
    private final UserMapper mapper;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(mapper.findById(id));
    }

    @Override
    public void save(@NonNull User user) {
        mapper.save(user);
    }
}
