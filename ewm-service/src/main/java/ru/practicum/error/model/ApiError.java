package ru.practicum.error.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class ApiError {
    List<StackTraceElement> errors;
    String message;
    String reason;
    String status;
    LocalDateTime timestamp;
}
