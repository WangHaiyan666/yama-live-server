package com.newverse.yama.live.infrastructure;

import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ComponentScan("com.newverse.yama.live.infrastructure")
@MapperScan(basePackages = "com.newverse.yama.live.infrastructure.mapper")
public class InfrastructureConfiguration {}
