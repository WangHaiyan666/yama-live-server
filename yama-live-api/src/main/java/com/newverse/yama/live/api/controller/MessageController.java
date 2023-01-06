package com.newverse.yama.live.api.controller;

import com.newverse.yama.live.api.spec.HeaderName;
import com.newverse.yama.live.api.spec.SwaggerTag;
import com.newverse.yama.live.domain.dto.MessageDto;
import com.newverse.yama.live.domain.service.MessageService;
import com.newverse.yama.live.domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
@Api(tags = SwaggerTag.MESSAGE)
public class MessageController {
    @NonNull
    private final UserService userService;
    @NonNull
    private final MessageService messageService;

    @PostMapping(path = "/pub/v1/topic/{topicId}/messages")
    @ApiOperation(value = "Publish a message", tags = { SwaggerTag.MESSAGE })
    public PublishResponse publish(
            @PathVariable @Min(1) long topicId,
            @RequestHeader(HeaderName.CSRF_TOKEN) String csrf,
            @RequestHeader(HeaderName.USER_TOKEN) String token,
            @RequestBody @Validated PublishRequest request
    ) {
        return new PublishResponse(
                messageService.publish(userService.getUser(token), topicId, request.getContent())
        );
    }

    @GetMapping(path = "/sub/v1/topic/{topicId}/message")
    @ApiOperation(value = "Get a message", tags = { SwaggerTag.MESSAGE })
    public GetResponse get(
            @PathVariable @Min(1) long topicId,
            @RequestHeader(HeaderName.USER_TOKEN) String token
    ) {
        return GetResponse.fromDto(messageService.get(userService.getUser(token), topicId));
    }

    @PutMapping(path = "/sub/v1/message/{messageId}/ack")
    @ApiOperation(value = "Ack a message", tags = { SwaggerTag.MESSAGE })
    public void ack(
            @PathVariable @Min(1) long messageId,
            @RequestHeader(HeaderName.CSRF_TOKEN) String csrf,
            @RequestHeader(HeaderName.USER_TOKEN) String token
    ) {
        messageService.ack(userService.getUser(token), messageId);
    }

    @Data
    public static class PublishRequest {
        @NotBlank
        @Length(max = 128000)
        private String content;
    }

    @Data
    @NoArgsConstructor
    @RequiredArgsConstructor
    public static class PublishResponse {
        @NonNull
        private Long messageId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetResponse {
        public static GetResponse fromDto(@NonNull MessageDto dto) {
            return new GetResponse(dto.getId(), dto.getContent());
        }

        private Long id;
        @NonNull
        private String content;
    }
}
