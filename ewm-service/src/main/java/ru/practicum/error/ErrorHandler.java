package ru.practicum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.error.exception.BadRequestException;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.DataNotFoundException;
import ru.practicum.error.model.ApiError;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<ApiError> handleBadRequestException(final BadRequestException badRequestException) {
        log.error("Error code: 400.", badRequestException);
        return new ResponseEntity<>(badRequestException.getApiError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleDataNotFoundException(final DataNotFoundException dataNotFoundException) {
        log.error("Error code: 404.", dataNotFoundException);
        return new ResponseEntity<>(dataNotFoundException.getApiError(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleConflictException(final ConflictException conflictException) {
        log.error("Error code: 409.", conflictException);
        return new ResponseEntity<>(conflictException.getApiError(), HttpStatus.CONFLICT);
    }
}


