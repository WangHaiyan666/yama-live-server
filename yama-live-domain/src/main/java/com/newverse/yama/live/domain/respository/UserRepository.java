package com.newverse.yama.live.domain.respository;

import com.newverse.yama.live.domain.entity.User;
import java.util.Optional;
import lombok.NonNull;

public interface UserRepository {
    Optional<User> findById(@NonNull Long id);

    void save(@NonNull User user);
}
