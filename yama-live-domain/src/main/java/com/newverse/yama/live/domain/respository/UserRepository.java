package com.newverse.yama.live.domain.respository;

import com.newverse.yama.live.domain.entity.User;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface UserRepository {
    Optional<User> findById(@NonNull Long id);

    void save(@NonNull User user);
}
