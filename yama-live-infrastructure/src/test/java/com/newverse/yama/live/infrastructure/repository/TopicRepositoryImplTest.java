package com.newverse.yama.live.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.newverse.yama.live.domain.entity.Topic;
import com.newverse.yama.live.infrastructure.mapper.TopicMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TopicRepositoryImplTest {
    @InjectMocks
    private TopicRepositoryImpl target;
    @Mock
    private TopicMapper mapper;

    @Test
    public void save_ok() {
        val topic = Topic.builder()
                         .name("name")
                         .userId(1L)
                         .eldestSequenceId(3L)
                         .createdAt(LocalDateTime.now())
                         .build();

        target.save(topic);

        verify(mapper, times(1)).save(topic);
    }

    @Test
    public void findByName_ok() {
        val name = "name";

        val topic = Topic.builder()
                         .name(name)
                         .userId(1L)
                         .eldestSequenceId(3L)
                         .createdAt(LocalDateTime.now())
                         .build();

        when(mapper.findByName(name)).thenReturn(topic);

        val actual = target.findByName(name);

        verify(mapper, times(1)).findByName(name);

        Assertions.assertThat(actual).isEqualTo(Optional.ofNullable(topic));
    }

    @Test
    public void findById_ok() {
        val id = 4L;

        val topic = Topic.builder()
                         .name("name")
                         .userId(1L)
                         .eldestSequenceId(3L)
                         .createdAt(LocalDateTime.now())
                         .build();

        when(mapper.findById(id)).thenReturn(topic);

        val actual = target.findById(id);

        verify(mapper, times(1)).findById(id);

        Assertions.assertThat(actual).isEqualTo(Optional.ofNullable(topic));
    }

    @Test
    public void updateEldestSequenceId_ok() {
        target.updateEldestSequenceId(5L, 6L);
        verify(mapper, times(1)).updateEldestSequenceId(5L, 6L);

    }
}


