package com.newverse.yama.live.domain.service;

import com.newverse.yama.live.domain.dto.MessageDto;
import com.newverse.yama.live.domain.entity.Message;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.exception.ApplicationException;
import com.newverse.yama.live.domain.respository.MessageRepository;
import com.newverse.yama.live.domain.respository.SubscriptionRepository;
import com.newverse.yama.live.domain.respository.TopicRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    @NonNull
    private final MessageRepository messageRepository;
    @NonNull
    private final TopicRepository topicRepository;
    @NonNull
    private final SubscriptionRepository subscriptionRepository;
    @NonNull
    private final Supplier<LocalDateTime> nowLocalDateTime;

    @Transactional
    public long publish(@NonNull User user, @NonNull Long topicId, @NonNull String content) {
        val topic = topicRepository.findById(topicId)
                                   .orElseThrow(() -> new ApplicationException(
                                           "Not found topic id: '" + topicId + "'.", HttpStatus.NOT_FOUND));

        if (topic.getUserId() != user.getId()) {
            throw new ApplicationException("This topic id: '" + topicId + "' is owned by other publisher.",
                                           HttpStatus.METHOD_NOT_ALLOWED);
        }

        val newSequenceId = topic.getEldestSequenceId() + 1L;

        System.out.println("===========thread id: " + Thread.currentThread().getId());

        val message = Message.builder()
                             .topicId(topic.getId())
                             .content(Base64Utils.encodeToString(content.getBytes(StandardCharsets.UTF_8)))
                             .userId(user.getId())
                             .createdAt(nowLocalDateTime.get())
                             .sequenceId(newSequenceId)
                             .build();

        messageRepository.save(message);

        topicRepository.updateEldestSequenceId(newSequenceId, topic.getId());

        return message.getId();
    }

    public MessageDto get(@NonNull User user, @NonNull Long topicId) {
        val subscription = subscriptionRepository.findByUserIdAndTopicId(user.getId(), topicId)
                                                 .orElseThrow(() -> new ApplicationException(
                                                         "Need to subscribe this topic id: " + topicId,
                                                         HttpStatus.METHOD_NOT_ALLOWED));

        return messageRepository
                .findByTopicIdAndSequenceId(
                        topicId,
                        subscription.getAckedSequenceId() + 1L)
                .map(it -> new MessageDto(it.getId(),
                                          new String(Base64Utils.decodeFromString(it.getContent()))))
                .orElse(MessageDto.noNewMessge());
    }

    @Transactional
    public void ack(@NonNull User user, @NonNull Long messageId) {
        val message = messageRepository.findById(messageId)
                                       .orElseThrow(() -> new ApplicationException(
                                               "No message found id: " + messageId,
                                               HttpStatus.NOT_FOUND));

        val subscription = subscriptionRepository.findByUserIdAndTopicId(user.getId(), message.getTopicId())
                                                 .orElseThrow(() -> new ApplicationException(
                                                         "Need to subscribe this topic id: "
                                                         + message.getTopicId(),
                                                         HttpStatus.METHOD_NOT_ALLOWED));

        if (message.getSequenceId() == subscription.getAckedSequenceId() + 1L) {
            subscriptionRepository.updateAckedSequenceId(message.getSequenceId(),
                                                         user.getId(),
                                                         message.getTopicId());
        } else {
            throw new ApplicationException(
                    "The message is acked or you have an older message that hasn't been acked.",
                    HttpStatus.BAD_REQUEST);
        }
    }
}
