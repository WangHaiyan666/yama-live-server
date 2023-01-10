package com.newverse.yama.live.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author he.qiming
 */
@AllArgsConstructor
public enum UserStatusEnum {

    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 封禁
     */
    BLOCKED(1),

    /**
     * 删除
     */
    DELETE(2);

    @Getter
    private final int state;

    private static final Map<Integer, UserStatusEnum> MAP;

    static {
        MAP =
                Arrays.stream(UserStatusEnum.values())
                        .collect(Collectors.toMap(UserStatusEnum::getState, state -> state));
    }

    public static UserStatusEnum parse(Integer state) {
        return MAP.get(state);
    }
}
