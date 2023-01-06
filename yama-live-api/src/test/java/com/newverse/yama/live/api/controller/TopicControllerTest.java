package com.newverse.yama.live.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.newverse.yama.live.api.controller.TopicController.RegisterRequest;
import com.newverse.yama.live.api.controller.TopicController.RegisterResponse;
import com.newverse.yama.live.api.controller.TopicController.SubscribeRequest;
import com.newverse.yama.live.api.controller.TopicController.SubscribeResponse;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.service.TopicService;
import com.newverse.yama.live.domain.service.UserService;
import com.newverse.yama.live.domain.spec.UserRole;
import java.time.LocalDateTime;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TopicControllerTest {
    @InjectMocks
    private TopicController target;
    @Mock
    private TopicService topicService;
    @Mock
    private UserService userService;

    @Test
    public void register_ok() {
        val userToken = "ROLE_4";
        val user = User.builder()
                       .id(4L)
                       .role(UserRole.PUBLISHER)
                       .name("111")
                       .createdAt(LocalDateTime.now())
                       .build();

        when(userService.getUser(userToken)).thenReturn(user);
        when(topicService.register(user, "topic")).thenReturn(3L);

        val request = new RegisterRequest();
        request.setName("topic");

        val actual = target.register("csrf", userToken, request);
        assertThat(actual).isEqualTo(new RegisterResponse(3L));

        verify(userService, times(1)).getUser(userToken);
        verify(topicService, times(1)).register(user, "topic");
    }

    @Test
    public void subscribe_ok() {
        val userToken = "ROLE_4";
        val user = User.builder()
                       .id(4L)
                       .role(UserRole.SUBSCRIBER)
                       .name("111")
                       .createdAt(LocalDateTime.now())
                       .build();

        when(userService.getUser(userToken)).thenReturn(user);
        when(topicService.subscribe(user, "topic")).thenReturn(3L);

        val request = new SubscribeRequest();
        request.setName("topic");

        val actual = target.subscribe("csrf", userToken, request);
        assertThat(actual).isEqualTo(new SubscribeResponse(3L));

        verify(userService, times(1)).getUser(userToken);
        verify(topicService, times(1)).subscribe(user, "topic");
    }
}

