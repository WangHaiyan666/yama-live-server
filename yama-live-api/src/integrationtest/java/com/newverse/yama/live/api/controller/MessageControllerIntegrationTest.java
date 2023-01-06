package com.newverse.yama.live.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import com.newverse.yama.live.api.CommonIntegrationTest;
import com.newverse.yama.live.api.spec.HeaderName;
import com.newverse.yama.live.domain.entity.Subscription;
import com.newverse.yama.live.domain.entity.Topic;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.respository.MessageRepository;
import com.newverse.yama.live.domain.respository.SubscriptionRepository;
import com.newverse.yama.live.domain.respository.TopicRepository;
import com.newverse.yama.live.domain.respository.UserRepository;
import com.newverse.yama.live.domain.spec.UserRole;
import java.time.LocalDateTime;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

public class MessageControllerIntegrationTest extends CommonIntegrationTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2000, 1, 1, 1, 1);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private MessageRepository messageRepository;

    private User publisher1;

    private User publisher2;

    private User subscriber1;

    private User subscriber2;

    @BeforeEach
    public void before() {
        publisher1 = User.builder().name("publisher_test")
                         .role(UserRole.PUBLISHER).createdAt(NOW).build();
        userRepository.save(publisher1);

        publisher2 = User.builder().name("publisher_test_2")
                         .role(UserRole.PUBLISHER).createdAt(NOW).build();
        userRepository.save(publisher2);

        subscriber1 = User.builder().name("subscriber_test")
                          .role(UserRole.SUBSCRIBER).createdAt(NOW).build();
        userRepository.save(subscriber1);

        subscriber2 = User.builder().name("subscriber_test_2")
                          .role(UserRole.SUBSCRIBER).createdAt(NOW).build();
        userRepository.save(subscriber2);
    }

    @Test
    @Transactional
    public void publish_ok() {
        val content = "n".repeat(128000);

        val topic = Topic.builder()
                         .name("name")
                         .userId(publisher1.getId())
                         .eldestSequenceId(3L)
                         .createdAt(LocalDateTime.now())
                         .build();

        topicRepository.save(topic);

        val subscription = Subscription.builder()
                                       .userId(subscriber1.getId())
                                       .ackedSequenceId(topic.getEldestSequenceId())
                                       .topicId(topic.getId())
                                       .createdAt(LocalDateTime.now())
                                       .build();

        subscriptionRepository.save(subscription);

        val request = new MessageController.PublishRequest();
        request.setContent(content);

        Assertions.assertTimeout(TIME_OUT, () -> {
            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/pub/v1/topic/" + topic.getId() + "/messages")
                                    .header(HeaderName.USER_TOKEN,
                                            "ROLE_" + publisher1.getId())
                                    .header(HeaderName.CSRF_TOKEN, "csrf")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isOk());

            val response = mockMvc.perform(MockMvcRequestBuilders
                                                   .get("/api/sub/v1/topic/" + topic.getId() + "/message")
                                                   .header(HeaderName.USER_TOKEN,
                                                           "ROLE_" + subscriber1.getId()))
                                  .andExpect(MockMvcResultMatchers.status().isOk())
                                  .andReturn();

            val message = objectMapper.readValue(response.getResponse().getContentAsString(),
                                                 MessageController.GetResponse.class);

            assertThat(message.getContent()).isEqualTo(content);
        });
    }

    @Test
    @Transactional
    public void publish_ok_2() {
        val content = "[ _`~!@#$%^&*你極()+=|{}':;',\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t\"";

        val topic = Topic.builder()
                         .name("name")
                         .userId(publisher1.getId())
                         .eldestSequenceId(3L)
                         .createdAt(LocalDateTime.now())
                         .build();

        topicRepository.save(topic);

        val subscription = Subscription.builder()
                                       .userId(subscriber1.getId())
                                       .ackedSequenceId(topic.getEldestSequenceId())
                                       .topicId(topic.getId())
                                       .createdAt(LocalDateTime.now())
                                       .build();

        subscriptionRepository.save(subscription);

        val request = new MessageController.PublishRequest();
        request.setContent(content);

        Assertions.assertTimeout(TIME_OUT, () -> {
            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/pub/v1/topic/" + topic.getId() + "/messages")
                                    .header(HeaderName.USER_TOKEN,
                                            "ROLE_" + publisher1.getId())
                                    .header(HeaderName.CSRF_TOKEN, "csrf")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isOk());

            val response = mockMvc.perform(MockMvcRequestBuilders
                                                   .get("/api/sub/v1/topic/" + topic.getId() + "/message")
                                                   .header(HeaderName.USER_TOKEN,
                                                           "ROLE_" + subscriber1.getId()))
                                  .andExpect(MockMvcResultMatchers.status().isOk())
                                  .andReturn();

            val message = objectMapper.readValue(response.getResponse().getContentAsByteArray(),
                                                 MessageController.GetResponse.class);

            val messagedb = messageRepository.findById(message.getId());

            assertThat(new String(Base64Utils.decodeFromString(messagedb.get().getContent()))).isEqualTo(
                    content);
            assertThat(message.getContent()).isEqualTo(content);
        });
    }

    @Test
    @Transactional
    public void publish_bad_parameter() {
        val content = "n".repeat(128001);
        val request = new MessageController.PublishRequest();

        val topic = Topic.builder()
                         .name("name")
                         .userId(publisher1.getId())
                         .eldestSequenceId(3L)
                         .createdAt(LocalDateTime.now())
                         .build();

        topicRepository.save(topic);

        request.setContent(content);
        Assertions.assertTimeout(TIME_OUT, () -> {
            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/pub/v1/topic/" + topic.getId() + "/messages")
                                    .header(HeaderName.USER_TOKEN,
                                            "ROLE_" + publisher1.getId())
                                    .header(HeaderName.CSRF_TOKEN, "csrf")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isBadRequest());
        });
    }
}
