package com.newverse.yama.live.domain.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {
    @NonNull
    private final String message;
    @NonNull
    private final HttpStatus status;
    private Throwable cause;

    public ApplicationException(@NonNull String message, @NonNull HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public ApplicationException(@NonNull String message, @NonNull Throwable cause, @NonNull HttpStatus status) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
        this.status = status;
    }
}
