package ru.practicum.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatClient {
    ResponseEntity<String> saveHit(String app, String uri, String ip, LocalDateTime timestamp);

    ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
