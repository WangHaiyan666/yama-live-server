package com.newverse.yama.live.api.controller;

import com.newverse.yama.live.api.spec.HeaderName;
import com.newverse.yama.live.api.spec.SwaggerTag;
import com.newverse.yama.live.domain.service.TopicService;
import com.newverse.yama.live.domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
@Api(tags = SwaggerTag.TOPIC)
public class TopicController {
    @NonNull
    private final TopicService topicService;
    @NonNull
    private final UserService userService;

    @PostMapping(path = "/pub/v1/topics")
    @ApiOperation(value = "Register a topic, (name maximum size : 255)", tags = SwaggerTag.TOPIC)
    public RegisterResponse register(
            @RequestHeader(HeaderName.CSRF_TOKEN) String csrf,
            @RequestHeader(HeaderName.USER_TOKEN) String token,
            @RequestBody @Validated RegisterRequest request
    ) {
        return new RegisterResponse(
                topicService.register(userService.getUser(token), request.getName())
        );
    }

    @PostMapping(path = "/sub/v1/topic/subscribe")
    @ApiOperation(value = "Subscribe a topic", tags = SwaggerTag.TOPIC)
    public SubscribeResponse subscribe(
            @RequestHeader(HeaderName.CSRF_TOKEN) String csrf,
            @RequestHeader(HeaderName.USER_TOKEN) String token,
            @RequestBody @Validated SubscribeRequest request
    ) {
        return new SubscribeResponse(
                topicService.subscribe(userService.getUser(token), request.getName())
        );
    }

    @Data
    public static class RegisterRequest {
        @NotBlank
        @Length(max = 255)
        private String name;
    }

    @Data
    public static class SubscribeRequest {
        @NotBlank
        private String name;
    }

    @Data
    @NoArgsConstructor
    @RequiredArgsConstructor
    public static class RegisterResponse {
        @NonNull
        private Long topicId;
    }

    @Data
    @NoArgsConstructor
    @RequiredArgsConstructor
    public static class SubscribeResponse {
        @NonNull
        private Long topicId;
    }
}
