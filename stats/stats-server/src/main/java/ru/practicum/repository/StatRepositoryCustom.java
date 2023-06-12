package ru.practicum.repository;

import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatRepositoryCustom {
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean isUnique);
}
