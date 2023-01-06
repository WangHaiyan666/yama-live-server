package com.newverse.yama.live.domain.respository;

import com.newverse.yama.live.domain.entity.Topic;
import java.util.Optional;

import lombok.NonNull;

public interface TopicRepository {
    void save(@NonNull Topic topic);

    Optional<Topic> findByName(@NonNull String name);

    Optional<Topic> findById(@NonNull Long id);

    void updateEldestSequenceId(@NonNull Long sequenceId, @NonNull Long id);
}
