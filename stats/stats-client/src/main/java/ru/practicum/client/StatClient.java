package ru.practicum.client;

import org.springframework.http.ResponseEntity;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatClient {
    ResponseEntity<String> saveHit(String app, String uri, String ip, LocalDateTime timestamp);

    ResponseEntity<List<ViewStatsDto>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
