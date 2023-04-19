package ru.practicum.client;

import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatClient {
    void saveHit(String app, String uri, String ip, LocalDateTime timestamp);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
