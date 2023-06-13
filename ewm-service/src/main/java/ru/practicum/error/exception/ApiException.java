package ru.practicum.error.exception;

import ru.practicum.error.model.ApiError;

public interface ApiException {
    ApiError getApiError();
}
