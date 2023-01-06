package com.newverse.yama.live.domain.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class MessageDto {
    private Long id;
    @NonNull
    private String content;

    public static MessageDto noNewMessge() {
        return new MessageDto(null, "no new message.");
    }
}
