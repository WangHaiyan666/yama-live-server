package com.newverse.yama.live.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.newverse.yama.live.domain.entity.Message;
import com.newverse.yama.live.infrastructure.mapper.MessageMapper;
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
public class MessageRepositoryImplTest {
    @InjectMocks
    private MessageRepositoryImpl target;
    @Mock
    private MessageMapper mapper;

    @Test
    public void save_ok() {
        val message = Message.builder()
                             .topicId(1L)
                             .content("content")
                             .userId(2L)
                             .createdAt(LocalDateTime.now())
                             .sequenceId(3L)
                             .build();

        target.save(message);

        verify(mapper, times(1)).save(message);
    }

    @Test
    public void findByTopicIdAndSequenceId_ok() {
        val message = Message.builder()
                             .topicId(1L)
                             .content("content")
                             .userId(2L)
                             .createdAt(LocalDateTime.now())
                             .sequenceId(3L)
                             .build();

        when(mapper.findByTopicIdAndSequenceId(1L, 3L)).thenReturn(message);

        val actual = target.findByTopicIdAndSequenceId(1L, 3L);

        verify(mapper, times(1)).findByTopicIdAndSequenceId(1L, 3L);

        Assertions.assertThat(actual).isEqualTo(Optional.ofNullable(message));
    }

    @Test
    public void findById_ok() {
        val message = Message.builder()
                             .topicId(1L)
                             .content("content")
                             .userId(2L)
                             .createdAt(LocalDateTime.now())
                             .sequenceId(3L)
                             .build();

        when(mapper.findById(1L)).thenReturn(message);

        val actual = target.findById(1L);

        verify(mapper, times(1)).findById(1L);

        Assertions.assertThat(actual).isEqualTo(Optional.ofNullable(message));
    }
}

