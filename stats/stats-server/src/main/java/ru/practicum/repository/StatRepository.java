package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("SELECT NEW ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.uri IN :uris " +
            "AND (h.timestamp BETWEEN :start AND :end) " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStatsDto> getStatsByUrisAndBetweenStartAndEndGroupByUri(
            LocalDateTime start,
            LocalDateTime end,
            Collection<String> uris
    );

    @Query("SELECT NEW ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM EndpointHit h " +
            "WHERE h.uri IN :uris " +
            "AND (h.timestamp BETWEEN :start AND :end) " +
            "GROUP BY h.uri, h.app " +
            "ORDER BY COUNT(DISTINCT(h.ip)) DESC")
    List<ViewStatsDto> getUniqueStatsByUrisAndBetweenStartAndEndGroupByUri(
            LocalDateTime start,
            LocalDateTime end,
            Collection<String> uris
    );
}

