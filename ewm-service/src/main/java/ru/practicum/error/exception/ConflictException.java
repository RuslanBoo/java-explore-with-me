package ru.practicum.error.exception;

public class ConflictException extends ApiExceptionImpl {
    public ConflictException(String message) {
        super(message, "Integrity constraint has been violated.", "CONFLICT");
    }
}
