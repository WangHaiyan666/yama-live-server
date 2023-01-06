package com.newverse.yama.live.api.configuration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class TimeConfiguration {
    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Asia/Tokyo"));
    }

    @Bean
    public Supplier<LocalDateTime> nowLocalDateTime() {
        return () -> LocalDateTime.now(clock());
    }
}
