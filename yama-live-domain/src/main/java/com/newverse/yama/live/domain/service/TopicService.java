package com.newverse.yama.live.domain.service;

import com.newverse.yama.live.domain.entity.Subscription;
import com.newverse.yama.live.domain.entity.Topic;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.exception.ApplicationException;
import com.newverse.yama.live.domain.respository.SubscriptionRepository;
import com.newverse.yama.live.domain.respository.TopicRepository;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicService {
    @NonNull
    private final TopicRepository topicRepository;
    @NonNull
    private final SubscriptionRepository subscriptionRepository;
    @NonNull
    private final Supplier<LocalDateTime> nowLocalDateTime;

    public long register(@NonNull User user, @NonNull String name) {
        try {
            val topic = Topic.builder()
                             .name(name)
                             .userId(user.getId())
                             .eldestSequenceId(0L)
                             .createdAt(nowLocalDateTime.get())
                             .build();
            topicRepository.save(topic);

            return topic.getId();
        } catch (DuplicateKeyException e) {
            throw new ApplicationException("This topic '" + name + "' is already been registered.",
                                           HttpStatus.CONFLICT);
        }
    }

    public long subscribe(@NonNull User user, @NonNull String name) {
        val topic = topicRepository.findByName(name)
                                   .orElseThrow(
                                           () -> new ApplicationException("Not found topic '" + name + "'.",
                                                                          HttpStatus.NOT_FOUND));

        try {
            subscriptionRepository.save(Subscription.builder()
                                                    .ackedSequenceId(
                                                            topic.getEldestSequenceId())
                                                    .topicId(topic.getId())
                                                    .userId(user.getId())
                                                    .createdAt(nowLocalDateTime.get())
                                                    .build());

            return topic.getId();
        } catch (DuplicateKeyException e) {
            throw new ApplicationException("Already subscribed to this topic: " + name, HttpStatus.CONFLICT);
        }
    }
}
