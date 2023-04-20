package ru.practicum.client;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface StatClient {
    ResponseEntity<Object> saveHit(String app, String uri, String ip, LocalDateTime timestamp);

    ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
