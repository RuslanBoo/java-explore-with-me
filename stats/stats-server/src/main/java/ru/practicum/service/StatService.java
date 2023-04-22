package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatService {
    private final StatRepository statRepository;
    private final EndpointHitMapper endpointHitMapper;

    public ResponseEntity<String> saveHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitMapper.fromDto(endpointHitDto);
        statRepository.save(endpointHit);

        return new ResponseEntity<>("Информация сохранена", HttpStatus.CREATED);
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean isUnique) {
        List<ViewStatsDto> stats;

        if (uris != null && !uris.isEmpty()) {

            if (isUnique) {
                stats = statRepository.getUniqueStatsByUrisAndBetweenStartAndEndGroupByUri(start, end, uris);
            } else {
                stats = statRepository.getStatsByUrisAndBetweenStartAndEndGroupByUri(start, end, uris);
            }
        } else {
            if (isUnique) {
                stats = statRepository.getUniqueStatsBetweenStartAndEndGroupByUri(start, end);
            } else {
                stats = statRepository.getStatsBetweenStartAndEndGroupByUri(start, end);
            }
        }

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
