package ru.practicum.error.exception;

import lombok.Getter;
import ru.practicum.error.model.ApiError;

import java.time.LocalDateTime;

@Getter
public class ApiExceptionImpl extends RuntimeException implements ApiException {
    private final String reason;
    private final String status;

    public ApiExceptionImpl(String message, String reason, String status) {
        super(message);
        this.reason = reason;
        this.status = status;
    }

    @Override
    public ApiError getApiError() {
        return ApiError.builder()
                .message(this.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
