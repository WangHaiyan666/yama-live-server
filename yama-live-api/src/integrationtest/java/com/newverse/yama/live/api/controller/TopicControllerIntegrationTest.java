package com.newverse.yama.live.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import com.newverse.yama.live.api.CommonIntegrationTest;
import com.newverse.yama.live.api.controller.TopicController.RegisterRequest;
import com.newverse.yama.live.api.controller.TopicController.RegisterResponse;
import com.newverse.yama.live.api.controller.TopicController.SubscribeRequest;
import com.newverse.yama.live.api.controller.TopicController.SubscribeResponse;
import com.newverse.yama.live.api.spec.HeaderName;
import com.newverse.yama.live.domain.entity.Subscription;
import com.newverse.yama.live.domain.entity.Topic;
import com.newverse.yama.live.domain.entity.User;
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

public class TopicControllerIntegrationTest extends CommonIntegrationTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2000, 1, 1, 1, 1);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private User publisher;

    private User subscriber;

    @BeforeEach
    public void before() {
        publisher = User.builder().name("publisher_test")
                        .role(UserRole.PUBLISHER).createdAt(NOW).build();
        userRepository.save(publisher);

        subscriber = User.builder().name("subscriber_test")
                         .role(UserRole.SUBSCRIBER).createdAt(NOW).build();
        userRepository.save(subscriber);
    }

//------------------ register ----------------------------------------------------------------------

    @Test
    @Transactional
    public void register_ok() {
        val name = "n".repeat(255);

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new RegisterRequest();
            request.setName(name);
            val response = mockMvc.perform(MockMvcRequestBuilders.post("/api/pub/v1/topics")
                                                                 .header(HeaderName.USER_TOKEN,
                                                                         "ROLE_" + publisher.getId())
                                                                 .header(HeaderName.CSRF_TOKEN, "csrf")
                                                                 .contentType(MediaType.APPLICATION_JSON)
                                                                 .content(objectMapper
                                                                                  .writeValueAsString(request)))
                                  .andExpect(MockMvcResultMatchers.status().isOk())
                                  .andReturn();

            val result = objectMapper.readValue(response.getResponse().getContentAsString(),
                                                RegisterResponse.class);

            val actual = topicRepository.findByName(name);
            assertThat(actual).isPresent();
            assertThat(actual.get().getId()).isGreaterThan(0L);
            assertThat(actual.get().getId()).isEqualTo(result.getTopicId());
            assertThat(actual.get().getName()).isEqualTo(name);
            assertThat(actual.get().getEldestSequenceId()).isEqualTo(0L);
            assertThat(actual.get().getUserId()).isEqualTo(publisher.getId());
        });
    }

    @Test
    @Transactional
    public void register_ok_2() {
        val name = "[ _`~!@#$%^&*你極()+=|{}':;',\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t\"";

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new RegisterRequest();
            request.setName(name);
            val response = mockMvc.perform(MockMvcRequestBuilders.post("/api/pub/v1/topics")
                                                                 .header(HeaderName.USER_TOKEN,
                                                                         "ROLE_" + publisher.getId())
                                                                 .header(HeaderName.CSRF_TOKEN, "csrf")
                                                                 .contentType(MediaType.APPLICATION_JSON)
                                                                 .content(objectMapper
                                                                                  .writeValueAsString(request)))
                                  .andExpect(MockMvcResultMatchers.status().isOk())
                                  .andReturn();

            val result = objectMapper.readValue(response.getResponse().getContentAsString(),
                                                RegisterResponse.class);

            val actual = topicRepository.findByName(name);
            assertThat(actual).isPresent();
            assertThat(actual.get().getId()).isGreaterThan(0L);
            assertThat(actual.get().getId()).isEqualTo(result.getTopicId());
            assertThat(actual.get().getName()).isEqualTo(name);
            assertThat(actual.get().getEldestSequenceId()).isEqualTo(0L);
            assertThat(actual.get().getUserId()).isEqualTo(publisher.getId());
        });
    }

    @Test
    @Transactional
    public void register_401() {
        val name = "name_test";

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new RegisterRequest();
            request.setName(name);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/pub/v1/topics")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + 0)
                                                  .header(HeaderName.CSRF_TOKEN, "csrf")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isUnauthorized());

            val actual = topicRepository.findByName(name);
            assertThat(actual).isEmpty();
        });
    }

    @Test
    @Transactional
    public void register_401_2() {
        val name = "name_test";

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new RegisterRequest();
            request.setName(name);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/pub/v1/topics")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + subscriber.getId())
                                                  .header(HeaderName.CSRF_TOKEN, "")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isUnauthorized());

            val actual = topicRepository.findByName(name);
            assertThat(actual).isEmpty();
        });
    }

    @Test
    @Transactional
    public void register_403() {
        val name = "name_test";

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new RegisterRequest();
            request.setName(name);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/pub/v1/topics")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + subscriber.getId())
                                                  .header(HeaderName.CSRF_TOKEN, "csrf")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isForbidden());

            val actual = topicRepository.findByName(name);
            assertThat(actual).isEmpty();
        });
    }

    @Test
    @Transactional
    public void register_bad_parameter() {
        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new RegisterRequest();
            request.setName("");
            mockMvc.perform(MockMvcRequestBuilders.post("/api/pub/v1/topics")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + publisher.getId())
                                                  .header(HeaderName.CSRF_TOKEN, "csrf")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isBadRequest());

            val actual = topicRepository.findByName("");
            assertThat(actual).isEmpty();

            val request2 = new RegisterRequest();
            request2.setName("n".repeat(256));
            mockMvc.perform(MockMvcRequestBuilders.post("/api/pub/v1/topics")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + publisher.getId())
                                                  .header(HeaderName.CSRF_TOKEN, "csrf")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request2)))
                   .andExpect(MockMvcResultMatchers.status().isBadRequest());

            val actual2 = topicRepository.findByName("n".repeat(256));
            assertThat(actual2).isEmpty();
        });

    }

    @Test
    @Transactional
    public void register_duplicated() {
        val name = "name_test";

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new RegisterRequest();
            request.setName(name);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/pub/v1/topics")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + publisher.getId())
                                                  .header(HeaderName.CSRF_TOKEN, "csrf")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isOk());

            mockMvc.perform(MockMvcRequestBuilders.post("/api/pub/v1/topics")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + publisher.getId())
                                                  .header(HeaderName.CSRF_TOKEN, "csrf")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isConflict());

            val actual = topicRepository.findByName(name);
            assertThat(actual).isPresent();
        });
    }

    //------------------ subscribe ----------------------------------------------------------------------

    @Test
    @Transactional
    public void subscriber_ok() {
        val name = "topic_1";

        val topic = Topic.builder()
                         .name(name)
                         .userId(publisher.getId())
                         .eldestSequenceId(3L)
                         .createdAt(LocalDateTime.now())
                         .build();

        topicRepository.save(topic);

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new SubscribeRequest();
            request.setName(name);
            val response = mockMvc.perform(MockMvcRequestBuilders.post("/api/sub/v1/topic/subscribe")
                                                                 .header(HeaderName.USER_TOKEN,
                                                                         "ROLE_" + subscriber.getId())
                                                                 .header(HeaderName.CSRF_TOKEN, "csrf")
                                                                 .contentType(MediaType.APPLICATION_JSON)
                                                                 .content(objectMapper
                                                                                  .writeValueAsString(request)))
                                  .andExpect(MockMvcResultMatchers.status().isOk())
                                  .andReturn();

            val result = objectMapper.readValue(response.getResponse().getContentAsString(),
                                                SubscribeResponse.class);

            val actual = subscriptionRepository.findByUserIdAndTopicId(subscriber.getId(), topic.getId());
            assertThat(actual).isPresent();
            assertThat(actual.get().getId()).isGreaterThan(0L);
            assertThat(actual.get().getTopicId()).isEqualTo(result.getTopicId());
            assertThat(actual.get().getTopicId()).isEqualTo(topic.getId());
            assertThat(actual.get().getUserId()).isEqualTo(subscriber.getId());
            assertThat(actual.get().getAckedSequenceId()).isEqualTo(topic.getEldestSequenceId());
        });
    }

    @Test
    @Transactional
    public void subscriber_topic_403() {
        val name = "n".repeat(255);

        val topic = Topic.builder()
                         .name(name)
                         .userId(publisher.getId())
                         .eldestSequenceId(3L)
                         .createdAt(LocalDateTime.now())
                         .build();

        topicRepository.save(topic);

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new SubscribeRequest();
            request.setName(name);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/sub/v1/topic/subscribe")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + publisher.getId())
                                                  .header(HeaderName.CSRF_TOKEN, "csrf")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isForbidden());

            val actual = subscriptionRepository.findByUserIdAndTopicId(subscriber.getId(), topic.getId());
            assertThat(actual).isEmpty();
        });
    }

    @Test
    @Transactional
    public void subscriber_topic_not_found() {
        val name = "n".repeat(255);

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new SubscribeRequest();
            request.setName(name);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/sub/v1/topic/subscribe")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + subscriber.getId())
                                                  .header(HeaderName.CSRF_TOKEN, "csrf")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isNotFound());
        });
    }

    @Test
    @Transactional
    public void subscriber_duplicated() {
        val name = "n".repeat(255);

        val topic = Topic.builder()
                         .name(name)
                         .userId(publisher.getId())
                         .eldestSequenceId(3L)
                         .createdAt(LocalDateTime.now())
                         .build();

        topicRepository.save(topic);

        val subscription = Subscription.builder()
                                       .userId(subscriber.getId())
                                       .ackedSequenceId(topic.getEldestSequenceId())
                                       .topicId(topic.getId())
                                       .createdAt(LocalDateTime.now())
                                       .build();

        subscriptionRepository.save(subscription);

        Assertions.assertTimeout(TIME_OUT, () -> {
            val request = new SubscribeRequest();
            request.setName(name);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/sub/v1/topic/subscribe")
                                                  .header(HeaderName.USER_TOKEN, "ROLE_" + subscriber.getId())
                                                  .header(HeaderName.CSRF_TOKEN, "csrf")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(request)))
                   .andExpect(MockMvcResultMatchers.status().isConflict());
        });
    }
}
