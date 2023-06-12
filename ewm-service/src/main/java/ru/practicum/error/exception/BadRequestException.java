package ru.practicum.error.exception;

public class BadRequestException extends ApiExceptionImpl {
    public BadRequestException(String message) {
        super(message, "Incorrectly made request.", "BAD_REQUEST");
    }
}
