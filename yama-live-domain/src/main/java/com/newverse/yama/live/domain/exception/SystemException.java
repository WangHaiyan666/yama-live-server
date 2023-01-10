package com.newverse.yama.live.domain.exception;

import lombok.NonNull;

/**
 * @author liangyu
 * @version 1.0
 * @since 2022/2/6 6:02 PM
 */

public class SystemException extends RuntimeException {

    public SystemException(@NonNull String message) {
        super(message);
    }

    public SystemException(Exception ex) {
        super(ex);
    }
}
