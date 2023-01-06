package com.newverse.yama.live.infrastructure.repository;

import com.newverse.yama.live.domain.entity.Message;
import com.newverse.yama.live.domain.respository.MessageRepository;
import com.newverse.yama.live.infrastructure.mapper.MessageMapper;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MessageRepositoryImpl implements MessageRepository {
    @NonNull
    private final MessageMapper mapper;

    @Override
    public Optional<Message> findByTopicIdAndSequenceId(@NonNull Long topicId, @NonNull Long sequenceId) {
        return Optional.ofNullable(mapper.findByTopicIdAndSequenceId(topicId, sequenceId));
    }

    @Override
    public void save(@NonNull Message message) {
        mapper.save(message);
    }

    @Override
    public Optional<Message> findById(@NonNull Long id) {
        return Optional.ofNullable(mapper.findById(id));
    }
}
