package com.newverse.yama.live.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.newverse.yama.live.api.controller.MessageController.GetResponse;
import com.newverse.yama.live.api.controller.MessageController.PublishRequest;
import com.newverse.yama.live.api.controller.MessageController.PublishResponse;
import com.newverse.yama.live.domain.dto.MessageDto;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.service.MessageService;
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
public class MessageControllerTest {
    @InjectMocks
    private MessageController target;
    @Mock
    private MessageService messageService;
    @Mock
    private UserService userService;

    @Test
    public void publish_ok() {
        val userToken = "ROLE_4";
        val topicId = 1L;
        val user = User.builder()
                       .id(4L)
                       .role(UserRole.PUBLISHER)
                       .name("111")
                       .createdAt(LocalDateTime.now())
                       .build();

        when(userService.getUser(userToken)).thenReturn(user);
        when(messageService.publish(user, topicId, "content")).thenReturn(3L);

        val request = new PublishRequest();
        request.setContent("content");

        val actual = target.publish(topicId, "csrf", userToken, request);
        assertThat(actual).isEqualTo(new PublishResponse(3L));

        verify(userService, times(1)).getUser(userToken);
        verify(messageService, times(1)).publish(user, topicId, "content");
    }

    @Test
    public void get_ok() {
        val userToken = "ROLE_4";
        val topicId = 1L;
        val dto = new MessageDto(3L, "content");
        val user = User.builder()
                       .id(4L)
                       .role(UserRole.SUBSCRIBER)
                       .name("111")
                       .createdAt(LocalDateTime.now())
                       .build();

        when(userService.getUser(userToken)).thenReturn(user);
        when(messageService.get(user, topicId)).thenReturn(dto);

        val actual = target.get(topicId, userToken);
        assertThat(actual).isEqualTo(GetResponse.fromDto(dto));

        verify(userService, times(1)).getUser(userToken);
        verify(messageService, times(1)).get(user, topicId);
    }

    @Test
    public void ack_ok() {
        val userToken = "ROLE_4";
        val messageId = 1L;
        val user = User.builder()
                       .id(4L)
                       .role(UserRole.SUBSCRIBER)
                       .name("111")
                       .createdAt(LocalDateTime.now())
                       .build();

        when(userService.getUser(userToken)).thenReturn(user);

        target.ack(messageId, "csrf", userToken);

        verify(userService, times(1)).getUser(userToken);
        verify(messageService, times(1)).ack(user, messageId);
    }
}

