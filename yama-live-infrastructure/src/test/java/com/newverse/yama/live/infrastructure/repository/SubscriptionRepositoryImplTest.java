package com.newverse.yama.live.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.newverse.yama.live.domain.entity.Subscription;
import com.newverse.yama.live.infrastructure.mapper.SubscriptionMapper;
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
public class SubscriptionRepositoryImplTest {
    @InjectMocks
    private SubscriptionRepositoryImpl target;
    @Mock
    private SubscriptionMapper mapper;

    @Test
    public void save_ok() {
        val subscription = Subscription.builder()
                                       .userId(1L)
                                       .ackedSequenceId(3L)
                                       .topicId(2L)
                                       .createdAt(LocalDateTime.now())
                                       .build();

        target.save(subscription);

        verify(mapper, times(1)).save(subscription);
    }

    @Test
    public void findByUserIdAndTopicId_ok() {
        val name = "name";

        val subscription = Subscription.builder()
                                       .userId(1L)
                                       .ackedSequenceId(3L)
                                       .topicId(2L)
                                       .createdAt(LocalDateTime.now())
                                       .build();

        when(mapper.findByUserIdAndTopicId(1L, 2L)).thenReturn(subscription);

        val actual = target.findByUserIdAndTopicId(1L, 2L);

        verify(mapper, times(1)).findByUserIdAndTopicId(1L, 2L);

        Assertions.assertThat(actual).isEqualTo(Optional.ofNullable(subscription));
    }

    @Test
    public void updateAckedSequenceId_ok() {
        target.updateAckedSequenceId(4L, 5L, 6L);
        verify(mapper, times(1)).updateAckedSequenceId(4L, 5L, 6L);

    }
}

