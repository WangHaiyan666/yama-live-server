package com.newverse.yama.live.domain.respository;

import com.newverse.yama.live.domain.entity.Message;
import java.util.Optional;

import lombok.NonNull;

public interface MessageRepository {
    Optional<Message> findByTopicIdAndSequenceId(@NonNull Long topicId, @NonNull Long sequenceId);

    void save(@NonNull Message message);

    Optional<Message> findById(@NonNull Long id);
}
