package com.newverse.yama.live.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.respository.UserRepository;
import com.newverse.yama.live.domain.spec.UserRole;
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
import org.springframework.security.authentication.AuthenticationServiceException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    @InjectMocks
    private UserService target;
    @Mock
    private UserRepository userRepository;

    @Test
    public void getUser_ok() {
        val user = User.builder()
                       .id(4L)
                       .role(UserRole.PUBLISHER)
                       .name("111")
                       .createdAt(LocalDateTime.now())
                       .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        val actual = target.getUser("ROLE_" + user.getId());

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual).isEqualTo(user);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void getUser_not_found() {
        val user = User.builder()
                       .id(4L)
                       .role(UserRole.PUBLISHER)
                       .name("111")
                       .createdAt(LocalDateTime.now())
                       .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> target.getUser("ROLE_" + user.getId())).isInstanceOf(
                AuthenticationServiceException.class);
        verify(userRepository, times(1)).findById(user.getId());
    }
}

