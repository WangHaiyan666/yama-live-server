package com.newverse.yama.live.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮箱是否被校验过的状态
 *
 * @author he.qiming
 */
@AllArgsConstructor
public enum UserEmailVerified {

    /**
     * 已校验
     */
    VERIFIED(1),

    /**
     * 未校验
     */
    NOT_VERIFIED(-1);

    @Getter
    private final int state;

}
