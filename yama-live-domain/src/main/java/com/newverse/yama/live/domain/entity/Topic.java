package com.newverse.yama.live.domain.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Topic {
    private long id;
    @NonNull
    private String name;
    @NonNull
    private Long userId;
    @NonNull
    private Long eldestSequenceId;
    @NonNull
    private LocalDateTime createdAt;
}
