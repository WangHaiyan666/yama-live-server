package com.newverse.yama.live.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.newverse.yama.live.domain.dto.MessageDto;
import com.newverse.yama.live.domain.entity.Message;
import com.newverse.yama.live.domain.entity.Subscription;
import com.newverse.yama.live.domain.entity.Topic;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.exception.ApplicationException;
import com.newverse.yama.live.domain.respository.MessageRepository;
import com.newverse.yama.live.domain.respository.SubscriptionRepository;
import com.newverse.yama.live.domain.respository.TopicRepository;
import com.newverse.yama.live.domain.spec.UserRole;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.util.Base64Utils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MessageServiceTest {
    @InjectMocks
    private MessageService target;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private MessageRepository messageRepository;
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
    public void publish_ok() {
        val content = "[ _`~!@#$%^&*你極()+=|{}':;',\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t\"";
        val now = LocalDateTime.now();
        val topic = Topic.builder()
                         .name("name")
                         .userId(user.getId())
                         .eldestSequenceId(1L)
                         .createdAt(now)
                         .build();
        val message = Message.builder()
                             .topicId(topic.getId())
                             .content(Base64Utils.encodeToString(content.getBytes(StandardCharsets.UTF_8)))
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(2L)
                             .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(topicRepository.findById(topic.getId())).thenReturn(Optional.of(topic));

        val actual = target.publish(user, topic.getId(), content);

        assertThat(actual).isEqualTo(message.getId());
        verify(topicRepository, times(1)).findById(topic.getId());
        verify(messageRepository, times(1)).save(message);
        verify(topicRepository, times(1)).updateEldestSequenceId(message.getSequenceId(), topic.getId());
    }

    @Test
    public void publish_topic_not_found() {
        val now = LocalDateTime.now();
        val topic = Topic.builder()
                         .name("name")
                         .userId(user.getId())
                         .eldestSequenceId(1L)
                         .createdAt(now)
                         .build();
        val message = Message.builder()
                             .topicId(topic.getId())
                             .content("content")
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(2L)
                             .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(topicRepository.findById(topic.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> target.publish(user, topic.getId(), "content")).isInstanceOf(
                ApplicationException.class);
        verify(topicRepository, times(1)).findById(topic.getId());
        verify(messageRepository, times(0)).save(message);
        verify(topicRepository, times(0)).updateEldestSequenceId(message.getSequenceId(), topic.getId());
    }

    @Test
    public void publish_not_the_owner() {
        val now = LocalDateTime.now();
        val topic = Topic.builder()
                         .name("name")
                         .userId(user.getId() + 1L)
                         .eldestSequenceId(1L)
                         .createdAt(now)
                         .build();
        val message = Message.builder()
                             .topicId(topic.getId())
                             .content("content")
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(2L)
                             .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(topicRepository.findById(topic.getId())).thenReturn(Optional.of(topic));

        assertThatThrownBy(() -> target.publish(user, topic.getId(), "content")).isInstanceOf(
                ApplicationException.class);
        verify(topicRepository, times(1)).findById(topic.getId());
        verify(messageRepository, times(0)).save(message);
        verify(topicRepository, times(0)).updateEldestSequenceId(message.getSequenceId(), topic.getId());
    }

    @Test
    public void get_ok() {
        val content = "[ _`~!@#$%^&*你極()+=|{}':;',\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t\"";
        val topicId = 2L;
        val now = LocalDateTime.now();
        val message = Message.builder()
                             .topicId(topicId)
                             .content(Base64Utils.encodeToString(content.getBytes(StandardCharsets.UTF_8)))
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(2L)
                             .build();
        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(1L)
                                       .topicId(topicId)
                                       .createdAt(now)
                                       .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(subscriptionRepository.findByUserIdAndTopicId(user.getId(), topicId)).thenReturn(
                Optional.of(subscription));
        when(messageRepository.findByTopicIdAndSequenceId(topicId, subscription.getAckedSequenceId() + 1L))
                .thenReturn(Optional.of(message));

        val actual = target.get(user, topicId);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(message.getId());
        assertThat(actual.getContent()).isEqualTo(content);
        verify(subscriptionRepository, times(1)).findByUserIdAndTopicId(user.getId(), topicId);
        verify(messageRepository, times(1)).findByTopicIdAndSequenceId(topicId,
                                                                       subscription.getAckedSequenceId() + 1L);
    }

    @Test
    public void get_no_new() {
        val topicId = 2L;
        val now = LocalDateTime.now();
        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(1L)
                                       .topicId(topicId)
                                       .createdAt(now)
                                       .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(subscriptionRepository.findByUserIdAndTopicId(user.getId(), topicId)).thenReturn(
                Optional.of(subscription));
        when(messageRepository.findByTopicIdAndSequenceId(topicId, subscription.getAckedSequenceId() + 1L))
                .thenReturn(Optional.empty());

        val actual = target.get(user, topicId);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getContent()).isEqualTo(MessageDto.noNewMessge().getContent());
        verify(subscriptionRepository, times(1)).findByUserIdAndTopicId(user.getId(), topicId);
        verify(messageRepository, times(1)).findByTopicIdAndSequenceId(topicId,
                                                                       subscription.getAckedSequenceId() + 1L);
    }

    @Test
    public void get_no_subscription() {
        val topicId = 2L;
        val now = LocalDateTime.now();
        val message = Message.builder()
                             .topicId(topicId)
                             .content("content")
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(2L)
                             .build();
        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(1L)
                                       .topicId(topicId)
                                       .createdAt(now)
                                       .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(subscriptionRepository.findByUserIdAndTopicId(user.getId(), topicId)).thenReturn(
                Optional.empty());
        when(messageRepository.findByTopicIdAndSequenceId(topicId, subscription.getAckedSequenceId() + 1L))
                .thenReturn(Optional.of(message));

        assertThatThrownBy(() -> target.get(user, topicId)).isInstanceOf(
                ApplicationException.class);
        verify(subscriptionRepository, times(1)).findByUserIdAndTopicId(user.getId(), topicId);
        verify(messageRepository, times(0)).findByTopicIdAndSequenceId(topicId,
                                                                       subscription.getAckedSequenceId() + 1L);
    }

    @Test
    public void ack_ok() {
        val now = LocalDateTime.now();
        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(1L)
                                       .topicId(1L)
                                       .createdAt(now)
                                       .build();
        val message = Message.builder()
                             .id(5L)
                             .topicId(subscription.getTopicId())
                             .content("content")
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(subscription.getAckedSequenceId() + 1L)
                             .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(subscriptionRepository.findByUserIdAndTopicId(user.getId(), message.getTopicId())).thenReturn(
                Optional.of(subscription));
        when(messageRepository.findById(message.getId()))
                .thenReturn(Optional.of(message));

        target.ack(user, message.getId());

        verify(subscriptionRepository, times(1)).findByUserIdAndTopicId(user.getId(), message.getTopicId());
        verify(messageRepository, times(1)).findById(message.getId());
        verify(subscriptionRepository, times(1)).updateAckedSequenceId(message.getSequenceId(), user.getId(),
                                                                       message.getTopicId());
    }

    @Test
    public void ack_message_not_found() {
        val now = LocalDateTime.now();
        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(1L)
                                       .topicId(1L)
                                       .createdAt(now)
                                       .build();
        val message = Message.builder()
                             .id(5L)
                             .topicId(subscription.getTopicId())
                             .content("content")
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(subscription.getAckedSequenceId() + 1L)
                             .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(subscriptionRepository.findByUserIdAndTopicId(user.getId(), message.getTopicId())).thenReturn(
                Optional.of(subscription));
        when(messageRepository.findById(message.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> target.ack(user, message.getId())).isInstanceOf(
                ApplicationException.class);
        verify(subscriptionRepository, times(0)).findByUserIdAndTopicId(user.getId(), message.getTopicId());
        verify(messageRepository, times(1)).findById(message.getId());
        verify(subscriptionRepository, times(0)).updateAckedSequenceId(message.getSequenceId(), user.getId(),
                                                                       message.getTopicId());
    }

    @Test
    public void ack_subscription_not_found() {
        val now = LocalDateTime.now();
        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(1L)
                                       .topicId(1L)
                                       .createdAt(now)
                                       .build();
        val message = Message.builder()
                             .id(5L)
                             .topicId(subscription.getTopicId())
                             .content("content")
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(subscription.getAckedSequenceId() + 1L)
                             .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(subscriptionRepository.findByUserIdAndTopicId(user.getId(), message.getTopicId())).thenReturn(
                Optional.empty());
        when(messageRepository.findById(message.getId()))
                .thenReturn(Optional.of(message));

        assertThatThrownBy(() -> target.ack(user, message.getId())).isInstanceOf(
                ApplicationException.class);
        verify(subscriptionRepository, times(1)).findByUserIdAndTopicId(user.getId(), message.getTopicId());
        verify(messageRepository, times(1)).findById(message.getId());
        verify(subscriptionRepository, times(0)).updateAckedSequenceId(message.getSequenceId(), user.getId(),
                                                                       message.getTopicId());
    }

    @Test
    public void ack_sequence_id_large_error() {
        val now = LocalDateTime.now();
        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(1L)
                                       .topicId(1L)
                                       .createdAt(now)
                                       .build();

        val message = Message.builder()
                             .id(5L)
                             .topicId(subscription.getTopicId())
                             .content("content")
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(subscription.getAckedSequenceId() + 2L)
                             .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(subscriptionRepository.findByUserIdAndTopicId(user.getId(), message.getTopicId())).thenReturn(
                Optional.of(subscription));
        when(messageRepository.findById(message.getId()))
                .thenReturn(Optional.of(message));

        assertThatThrownBy(() -> target.ack(user, message.getId())).isInstanceOf(
                ApplicationException.class);
        verify(subscriptionRepository, times(1)).findByUserIdAndTopicId(user.getId(), message.getTopicId());
        verify(messageRepository, times(1)).findById(message.getId());
        verify(subscriptionRepository, times(0)).updateAckedSequenceId(message.getSequenceId(), user.getId(),
                                                                       message.getTopicId());
    }

    @Test
    public void ack_sequence_id_small_error() {
        val now = LocalDateTime.now();
        val subscription = Subscription.builder()
                                       .userId(user.getId())
                                       .ackedSequenceId(1L)
                                       .topicId(1L)
                                       .createdAt(now)
                                       .build();

        val message = Message.builder()
                             .id(5L)
                             .topicId(subscription.getTopicId())
                             .content("content")
                             .userId(user.getId())
                             .createdAt(now)
                             .sequenceId(subscription.getAckedSequenceId())
                             .build();

        when(nowLocalDateTime.get()).thenReturn(now);
        when(subscriptionRepository.findByUserIdAndTopicId(user.getId(), message.getTopicId())).thenReturn(
                Optional.of(subscription));
        when(messageRepository.findById(message.getId()))
                .thenReturn(Optional.of(message));

        assertThatThrownBy(() -> target.ack(user, message.getId())).isInstanceOf(
                ApplicationException.class);
        verify(subscriptionRepository, times(1)).findByUserIdAndTopicId(user.getId(), message.getTopicId());
        verify(messageRepository, times(1)).findById(message.getId());
        verify(subscriptionRepository, times(0)).updateAckedSequenceId(message.getSequenceId(), user.getId(),
                                                                       message.getTopicId());
    }
}

