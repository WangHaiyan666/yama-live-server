package com.newverse.yama.live.domain.respository;

import com.newverse.yama.live.domain.entity.Subscription;
import java.util.Optional;

import lombok.NonNull;

public interface SubscriptionRepository {
    Optional<Subscription> findByUserIdAndTopicId(@NonNull Long userId, @NonNull Long topicId);

    void save(@NonNull Subscription subscription);

    void updateAckedSequenceId(@NonNull Long ackedSequenceId, @NonNull Long userId, @NonNull Long topicId);
}
