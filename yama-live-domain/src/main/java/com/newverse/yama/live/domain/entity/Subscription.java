package com.newverse.yama.live.domain.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Subscription {
    private long id;
    @NonNull
    private Long topicId;
    @NonNull
    private Long userId;
    @NonNull
    private Long ackedSequenceId;
    @NonNull
    private LocalDateTime createdAt;
}
