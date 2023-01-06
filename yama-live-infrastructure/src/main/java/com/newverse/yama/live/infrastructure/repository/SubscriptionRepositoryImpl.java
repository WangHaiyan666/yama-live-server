package com.newverse.yama.live.infrastructure.repository;

import com.newverse.yama.live.domain.entity.Subscription;
import com.newverse.yama.live.domain.respository.SubscriptionRepository;
import com.newverse.yama.live.infrastructure.mapper.SubscriptionMapper;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    @NonNull
    private final SubscriptionMapper mapper;

    @Override
    public Optional<Subscription> findByUserIdAndTopicId(@NonNull Long userId, @NonNull Long topicId) {
        return Optional.ofNullable(mapper.findByUserIdAndTopicId(userId, topicId));
    }

    @Override
    public void save(@NonNull Subscription subscription) {
        mapper.save(subscription);
    }

    @Override
    public void updateAckedSequenceId(@NonNull Long ackedSequenceId, @NonNull Long userId,
                                      @NonNull Long topicId) {
        mapper.updateAckedSequenceId(ackedSequenceId, userId, topicId);
    }
}
