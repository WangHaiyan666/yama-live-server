package com.newverse.yama.live.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.spec.UserRole;
import com.newverse.yama.live.infrastructure.mapper.UserMapper;
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
public class UserRepositoryImplTest {
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserRepositoryImpl target;

    @Test
    public void findById_ok() {
        val id = 3L;
        val user = User.builder().role(UserRole.PUBLISHER).name("111").createdAt(LocalDateTime.now()).build();
        when(
                mapper.findById(id)
        ).thenReturn(user);

        val actual = target.findById(id);

        verify(mapper, times(1)).findById(id);

        Assertions.assertThat(actual).isEqualTo(Optional.ofNullable(user));
    }

    @Test
    public void save_ok() {
        val user = User.builder().role(UserRole.PUBLISHER).name("111").createdAt(LocalDateTime.now()).build();
        target.save(user);
        verify(mapper, times(1)).save(user);
    }
}
