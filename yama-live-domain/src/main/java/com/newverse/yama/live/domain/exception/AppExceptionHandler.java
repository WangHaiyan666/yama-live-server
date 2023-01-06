package com.newverse.yama.live.domain.exception;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        final ErrorResponse response;
        if (e.getCause() == null) {
            response = new ErrorResponse(e.getMessage());
        } else {
            response = new ErrorResponse(e.getMessage(), e.getCause().getMessage());
        }

        return ResponseEntity.status(e.getStatus()).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        val errorMessage = new StringBuilder();
        val bindingResult = e.getBindingResult().getFieldErrors();

        for (FieldError fieldError : bindingResult) {
            errorMessage.append("Parameter'").append(fieldError.getField()).append(
                    "' is invalid because: ").append(fieldError.getDefaultMessage()).append(" | ");
            ;
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage.toString()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Throwable e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(e.getMessage(), e.getCause().getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR == status) {
            log.error("Unknown request binding error occurred", e);
        } else {
            log.warn("Unknown exception occurred.", e);
        }

        return ResponseEntity.status(status)
                             .body(new ErrorResponse(e.getMessage(), e.getCause().getMessage()));
    }

    @Value
    public static class ErrorResponse {
        @NonNull
        private Error error;

        public ErrorResponse(@NonNull String messge) {
            error = new Error(messge, null);
        }

        public ErrorResponse(@NonNull String messge, @NonNull String cause) {
            error = new Error(messge, cause);
        }

        @Value
        public static class Error {
            @NonNull
            private String message;

            private String cause;
        }
    }
}
