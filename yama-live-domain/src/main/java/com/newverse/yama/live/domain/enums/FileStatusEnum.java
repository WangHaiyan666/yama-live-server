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
public enum FileStatusEnum {

    /**
     * 初始
     */
    INITIAL(0),

    /**
     * 待上链
     */
    WAITING_UPLOAD(1),


    /**
     * 已上传
     */
    FINISHED_UPLOAD(2),


    /**
     * 待上链
     */
    WAITING_LINK(3),

    /**
     * 已上链
     */
    FINISHED_LINK(4),

    /**
     * 成功
     */
    SUCCESS(100),

    /**
     * 失败
     */
    FAIL(99),

    /**
     * 超时
     */
    TIMEOUT(98);

    @Getter
    private final int state;

    private static final Map<Integer, FileStatusEnum> MAP;

    static {
        MAP = Arrays.stream(FileStatusEnum.values()).collect(Collectors.toMap(FileStatusEnum::getState, state -> state));
    }

    public static FileStatusEnum parse(Integer state) {
        return MAP.get(state);
    }
}
