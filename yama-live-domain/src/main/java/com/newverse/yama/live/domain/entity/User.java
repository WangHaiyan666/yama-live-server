package com.newverse.yama.live.domain.entity;

import com.newverse.yama.live.domain.spec.UserRole;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class User {
    private long id;
    @NonNull
    private String name;
    @NonNull
    private UserRole role;
    @NonNull
    private LocalDateTime createdAt;
}
