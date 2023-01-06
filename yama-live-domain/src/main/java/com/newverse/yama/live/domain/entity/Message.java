package com.newverse.yama.live.domain.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Message {
    private long id;
    @NonNull
    private Long userId;
    @NonNull
    private Long topicId;
    @NonNull
    private Long sequenceId;
    @NonNull
    private String content;
    @NonNull
    private LocalDateTime createdAt;
}
