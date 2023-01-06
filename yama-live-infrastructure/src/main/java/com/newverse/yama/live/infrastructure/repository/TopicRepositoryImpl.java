package com.newverse.yama.live.infrastructure.repository;

import com.newverse.yama.live.domain.entity.Topic;
import com.newverse.yama.live.domain.respository.TopicRepository;
import com.newverse.yama.live.infrastructure.mapper.TopicMapper;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TopicRepositoryImpl implements TopicRepository {
    @NonNull
    private final TopicMapper mapper;

    @Override
    public void save(@NonNull Topic topic) {
        mapper.save(topic);
    }

    @Override
    public Optional<Topic> findByName(@NonNull String name) {
        return Optional.ofNullable(mapper.findByName(name));
    }

    @Override
    public Optional<Topic> findById(@NonNull Long id) {
        return Optional.ofNullable(mapper.findById(id));
    }

    @Override
    public void updateEldestSequenceId(@NonNull Long sequenceId, @NonNull Long id) {
        mapper.updateEldestSequenceId(sequenceId, id);
    }
}
