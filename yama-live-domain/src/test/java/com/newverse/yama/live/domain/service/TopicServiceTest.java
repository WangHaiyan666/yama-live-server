package com.newverse.yama.live.domain.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.newverse.yama.live.domain.entity.Subscription;
import com.newverse.yama.live.domain.entity.Topic;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.exception.ApplicationException;
import com.newverse.yama.live.domain.respository.SubscriptionRepository;
import com.newverse.yama.live.domain.respository.TopicRepository;
import com.newverse.yama.live.domain.spec.UserRole;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DuplicateKeyException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TopicServiceTest {
    @InjectMocks
    private TopicService target;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private Supplier<LocalDateTime> nowLocalDateTime;

    private User user = User.builder()
                            .role(UserRole.PUBLISHER)
                            .name("111")
                            .createdAt(LocalDateTime.now())
                            .build();

    @Test
    public void register_ok() {
        val now = LocalDateTime.now();

        val topic = Topic.builder()
                         .name("name")
                         .userId(user.getId())
                         .eldestSequenceId(0L)
                         .createdAt(now)
                         .build();

        when(nowLocalDateTime.get()).thenReturn(now);

        val actual = target.register(user, "name");

        verify(topicRepository, times(1)).save(topic);
        Assertions.assertThat(actual).isEqualTo(topic.getId());
    }

    @Test
    public void register_duplicated() {
        val now = LocalDateTime.now();

        val topic = Topic.builder()
                         .name("name")
                         .userId(user.getId())
                         .eldestSequenceId(0L)
                         .createdAt(now)
                         .build();

        when(nowLocalDateTime.get()).thenReturn(now);

        doThrow(new DuplicateKeyException("xxxx")).when(topicRepository).save(topic);

        assertThatThrownBy(() -> target.register(user, "name")).isInstanceOf(ApplicationException.class);
    }

    @Test
    public void subscribe_ok() {
        val name = "name";
        val now = LocalDateTime.now();

        val topic = Topic.builder()
                         .name(name)
                         .userId(user.getId())
                         .eldestSequenceId(2L)
                         .createdAt(now)
                         .build();

        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(topic.getEldestSequenceId())
                                       .topicId(topic.getId())
                                       .createdAt(now)
                                       .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(topicRepository.findByName(name)).thenReturn(Optional.of(topic));

        val actual = target.subscribe(user, name);

        Assertions.assertThat(actual).isEqualTo(topic.getId());
        verify(topicRepository, times(1)).findByName(name);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void subscribe_topic_not_found() {
        val name = "name";
        val now = LocalDateTime.now();

        val topic = Topic.builder()
                         .name(name)
                         .userId(user.getId())
                         .eldestSequenceId(2L)
                         .createdAt(now)
                         .build();

        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(topic.getEldestSequenceId())
                                       .topicId(topic.getId())
                                       .createdAt(now)
                                       .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(topicRepository.findByName(name)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> target.subscribe(user, "name")).isInstanceOf(ApplicationException.class);
        verify(topicRepository, times(1)).findByName(name);
        verify(subscriptionRepository, times(0)).save(subscription);
    }

    @Test
    public void subscribe_duplicated() {
        val name = "name";
        val now = LocalDateTime.now();

        val topic = Topic.builder()
                         .name(name)
                         .userId(user.getId())
                         .eldestSequenceId(2L)
                         .createdAt(now)
                         .build();

        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(topic.getEldestSequenceId())
                                       .topicId(topic.getId())
                                       .createdAt(now)
                                       .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(topicRepository.findByName(name)).thenReturn(Optional.of(topic));
        doThrow(new DuplicateKeyException("xxxx")).when(subscriptionRepository).save(subscription);

        assertThatThrownBy(() -> target.subscribe(user, "name")).isInstanceOf(ApplicationException.class);
        verify(topicRepository, times(1)).findByName(name);
    }
}

